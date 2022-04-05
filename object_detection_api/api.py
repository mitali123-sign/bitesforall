from flask import Flask, jsonify
from flask_restful import Resource, Api, reqparse
import werkzeug, os, json

import numpy as np
import pandas as pd
import tensorflow as tf
from PIL import Image
import urllib

app = Flask(__name__)
api = Api(app)
UPLOAD_FOLDER = 'static'
parser = reqparse.RequestParser()
parser.add_argument('file',type=werkzeug.datastructures.FileStorage, location='files')


def predict(model_path, file_path):
    # Load the TFLite model and allocate tensors.
    interpreter = tf.lite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()
    # Get input and output tensors.
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    # Make Input data
    input_shape = input_details[0]['shape']
    image_size = (input_shape[1], input_shape[2])
    img = Image.open(file_path)
    img = img.resize(image_size)
    num_img = np.array(img)
    num_img = num_img.reshape(input_shape)
    interpreter.set_tensor(input_details[0]['index'], num_img)
    interpreter.invoke()
    # Make Output data
    output_data = interpreter.get_tensor(output_details[0]['index'])
    num_det = int(interpreter.get_tensor(output_details[3]['index']))
    boxes = interpreter.get_tensor(output_details[0]['index'])[0][:num_det]
    classes = interpreter.get_tensor(output_details[1]['index'])[0][:num_det]
    scores = interpreter.get_tensor(output_details[2]['index'])[0][:num_det]
    indexs = [i for i in range(num_det) if scores[i] > 0.9] # score 가 높은 결과만 리턴할 예정
    
    # 라벨 이름 셋팅
    en_label_names = []
    with open("model/dict.txt", 'r') as labels:
        lines = labels.readlines()
        for line in lines:
            en_label_names.append(line.rstrip())
    print(en_label_names)
    label_names = []
    with open("model/ko_dict.txt", 'r', encoding='utf-8') as labels:
        lines = labels.readlines()
        for line in lines:
            label_names.append(line.rstrip())
    print(label_names)

    # 식품 영양 DB API 접근을 위한 셋팅
    with open('secret.json') as json_file:
        json_data = json.load(json_file)
    accesskey = json_data['accesskey']
    # 순서대로 음식명 열량 / 탄수화물 / 단백질 / 지방 / 당류 / 나트륨 / 콜레스테롤 / 포화지방산 / 트랜스지방
    nut_names = ['calorie', 'carbohydrate', 'protein', 'fat', 'sugars', 'sodium', 'cholesterol', 'fatty_acid', 'trans_fat']

    # 결과 리스트 만들기
    food_list = list()
    food_name_set = set() 
    for index in indexs:
        # 라벨 변환
        my_score = scores[index]
        my_label = label_names[int(classes[index])+1]
        my_en_label = en_label_names[int(classes[index])+1]
        food_dict = {'ko_label': my_label, 'en_label': my_en_label, 'score': my_score}
        # 중복 음식 저장 피하기
        if my_label in food_name_set: 
            continue
        else:
            food_name_set.add(my_label)
        # 식품 영양 DB API 호출
        try:
            foodname = my_label
            encoding_foodname = urllib.parse.quote(foodname)
            url = "http://openapi.foodsafetykorea.go.kr/api/"+accesskey+"/I2790/json/1/21/DESC_KOR="+encoding_foodname
            response = urllib.request.urlopen(url)
        except:
            print(my_label)
        else:
            json_str = response.read().decode("utf-8")
            json_object = json.loads(json_str)
            df = pd.json_normalize(json_object['I2790']['row'])
            df = df.fillna(0)
            print(df)
            for i, name in enumerate(nut_names):
                idxname = 'NUTR_CONT' + str(i+1)
                df[idxname] = pd.to_numeric(df[idxname], downcast="float")
                tmp = df[idxname][0]
                if np.isnan(tmp) or tmp == 0:
                    value = int(df[idxname].max())
                else:
                    value = int(df[idxname][0])
                food_dict[name] = value
        # 각 메뉴 저장
        food_list.append(food_dict)

    return food_list


class MyEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, np.integer):
            return int(obj)
        elif isinstance(obj, np.floating):
            return float(obj)
        elif isinstance(obj, np.ndarray):
            return obj.tolist()
        else:
            return super(MyEncoder, self).default(obj)


class PhotoUpload(Resource):
    def post(self):
        data = parser.parse_args()
        if data['file'] == "":
            return {
                    'data':'',
                    'message':'No file found',
                    'status':'error'
                    }

        photo = data['file']
        if photo:
            # 파일 저장
            filename = 'uploaded_image.' + photo.filename.split(".")[-1]
            filepath = os.path.join(UPLOAD_FOLDER,filename)
            photo.save(filepath)
            # 음식 정보 받아오기
            modelpath = "model/model.tflite"
            food_info = predict(modelpath, filepath)
            # 결과 리턴
            return json.dumps(food_info, cls=MyEncoder, ensure_ascii=False)

        return {
                'data':'',
                'message':'Something when wrong',
                'status':'error'
                }


api.add_resource(PhotoUpload,'/upload')

if __name__ == '__main__':
    app.run(debug=True)
