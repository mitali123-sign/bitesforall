# Object Detection API
## (1) Setting up a python virtual environment (Choose one of the two)
* In anaconda prompt
```
conda create -n "test" python=3.7
conda activate test
```
* or in local environment
```
python -m venv myproject
cd /venvs/myproject/Scripts
activate
```

## (2) Install libraries in virtual environment (Choose one of the two)
* Download using requirements.txt
```
pip install -r requirements.txt
```

* or download one by one
```
pip install flask
pip install flask-restful
pip install tensorflow==2.3.1
pip install pandas
pip install jsonify
pip install werkzeug
pip install pillow
```

## (3) Create an secret.json file
* Receive the food nutrition DB API key and enter it into the file
* [The Food Nutrition DB Link](https://www.foodsafetykorea.go.kr/api/openApiInfo.do?menu_grp=MENU_GRP31&menu_no=661&show_cnt=10&start_idx=1&svc_no=I2790&svc_type_cd=API_TYPE06)
```
{
    "accesskey": [YOUR API KEY]
}
```

## (4) Run server
```
python api.py
```