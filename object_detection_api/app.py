import numpy as np
import tensorflow as tf
from PIL import Image
from PIL import ImageDraw
import pandas as pd
import os

from flask import Flask, render_template, request
from werkzeug.utils import secure_filename
import urllib.request
import urllib
import json
from pandas.io.json import json_normalize

app = Flask(__name__)
app.config['TEMPLATES_AUTO_RELOAD'] = True


@app.route('/getimg')
def main():
    return render_template('upload.html')

# 저장한 이미지를 object detection 코드로 음식 이름값 str로 리턴하기


@app.route('/getfoodname')
def inputfoodname():
    return render_template('inputfoodname.html')

# 파일 업로드 처리 - 이미지 파일에 저장하기
@app.route('/imgModel', methods = ['GET', 'POST'])
def imgModel():
    if request.method == 'POST':
        f = request.files['file']
        # 저장할 경로 + 파일명
        f.save(secure_filename(f.filename))
        msg = "uploads success"

        # 예측
        # Load the TFLite model and allocate tensors.
        interpreter = tf.lite.Interpreter(model_path="model/model.tflite")
        interpreter.allocate_tensors()
        # Get input and output tensors.
        input_details = interpreter.get_input_details()
        output_details = interpreter.get_output_details()
        # Make Input data
        input_shape = input_details[0]['shape']
        image_size = (input_shape[1], input_shape[2])
        img = Image.open(f.filename)
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
        # Draw results
        df = pd.DataFrame(boxes)
        img_width, img_height = img.size
        df['ymin'] = df[0].apply(lambda y: max(1,(y*img_height)))
        df['xmin'] = df[1].apply(lambda x: max(1,(x*img_width)))
        df['ymax'] = df[2].apply(lambda y: min(img_height,(y*img_height)))
        df['xmax'] = df[3].apply(lambda x: min(img_width,(x * img_width)))
        boxes_scaled = df[['ymin', 'xmin', 'ymax', 'xmax']].to_numpy()
        draw = ImageDraw.Draw(img)
        label_names = []
        with open("model/ko_dict.txt", 'r', encoding='utf-8') as labels:
            lines = labels.readlines()
            for line in lines:
                label_names.append(line.rstrip())
        print(label_names)
        for i in range(num_det):
            if scores[i] > 0.5:
                x1, y1, x2, y2 = boxes_scaled[i]
                draw.rectangle(((x1, y1), (x2, y2)), outline=(0, 0, 255), width=1)
                msg = label_names[int(classes[i])+1]
        pred_filename = "pred_" + f.filename
        img.save("static/"+pred_filename)

        return render_template('after.html', data=msg, filename=pred_filename)

#이미지 이름 가져와서 영양소 값들 출력하기
@app.route('/getfoodname', methods=['POST'])
def getfoodname():
    #foodname 받아오기
    foodname=request.form['a']
    with open('secret.json') as json_file:
        json_data = json.load(json_file)
    accesskey=json_data['accesskey']
    encoding_foodname=urllib.parse.quote(foodname)

    url = "http://openapi.foodsafetykorea.go.kr/api/"+accesskey+"/I2790/json/1/21/DESC_KOR="+encoding_foodname

    response = urllib.request.urlopen(url)
    json_str = response.read().decode("utf-8")
    json_object = json.loads(json_str)

    df=pd.json_normalize(json_object['I2790']['row'])
    send_data_list=list()

    send_data_list.append(df['DESC_KOR'][0])

    for i in range(1,10):
    #순서대로 음식명 열량 / 탄수화물 / 단백질 / 지방 / 당류 / 나트륨 / 콜레스테롤 / 포화지방산 / 트랜스지방
        idxname='NUTR_CONT'
        idxname=idxname+str(i)
        value=df[idxname][0]
        send_data_list.append(value)

    send_data_list_num = len(send_data_list)

    return render_template('after2.html', data=send_data_list, num=send_data_list_num)


if __name__ == '__main__':
    # 서버 실행
    app.run(debug = True)
