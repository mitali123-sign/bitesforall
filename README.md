# 2021 Solution Challenge :  Everyone’s meal
* Team : Open Minder
* Members : Heyju Jun, Juyeon Lee, Haram Lee, Minhye Shin
- [2021 Solution Challenge :  Everyone’s meal](#2021-solution-challenge---everyones-meal)
  - [Demo video](#demo-video)
  - [How to run](#how-to-run)
    - [1. Download this project](#1-download-this-project)
    - [2. Run Object detection REST API server](#2-run-object-detection-rest-api-server)
    - [3. Run Spring boot backend server](#3-run-spring-boot-backend-server)
    - [4. Run React-native frontend client](#4-run-react-native-frontend-client)
  - [Skill](#skill)
  - [Contributors](#contributors)


## Demo video
* You can see demo video on YOUTUBE : [Everyone's Meal | Google DSC Solution Challenge 2021](https://youtu.be/c7C_P0GYEuc)
![Thumbnail](https://user-images.githubusercontent.com/35680202/113224348-2a8d1100-92c6-11eb-8312-2b4cc52cad44.png)


## How to run
### 1. Download this project
```
git clone https://github.com/dsc-sookmyung/2021-OpenMinder.git
```

### 2. Run Object detection REST API server
```
cd /path/to/2021-OpenMinder/object_detection_api/
```
#### (1) Setting up a python virtual environment (Choose one of the two)
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

#### (2) Install libraries in virtual environment (Choose one of the two)
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

#### (3) Create an secret.json file
* Receive the food nutrition DB API key and enter it into the file
* [The Food Nutrition DB Link](https://www.foodsafetykorea.go.kr/api/openApiInfo.do?menu_grp=MENU_GRP31&menu_no=661&show_cnt=10&start_idx=1&svc_no=I2790&svc_type_cd=API_TYPE06)
```
{
    "accesskey": [YOUR API KEY]
}
```

#### (4) Run server
```
python api.py
```

### 3. Run Spring boot backend server
```
cd /path/to/2021-OpenMinder/emeal
```
#### (1) Create an application.yml file
```
# in emeal/src/main/resources/application.yml
# Make GCP SQL instance and enter the following items

spring:
  datasource:
    hikari:
      jdbc-url: [YOUR DB URL]
      username: [YOUR DB USERNAME]
      password: [YOUR DB PASSWORD]
      driver-class-name: com.mysql.cj.jdbc.Driver
  servlet:
    multipart:
      enabled: true
      file-size-threshold: 2KB
      max-file-size: 200MB
      max-request-size: 215MB
      location: C:/Temp

mybatis:
  type-aliases-package: openminder.emeal.mapper
  mapper-locations: mybatis/mapper/**/*.xml

file:
  upload_dir: ./static/upload/avatar
  upload_picture_dir: ./static/upload/picture
```

#### (2) Build and Run server


### 4. Run React-native frontend client
```
cd /path/to/2021-OpenMinder/emeal/src/main/frontend/
```
#### (1) Put your localhost IP address in the ipConfig.js file
```
export const LOCAL = 'http://[YOUR IP ADDRESS]:8080';
```

#### (2) Install android emulator
* If you don't have NDK version `21.0.6113669` in Android Studio SDK Tools, install it.

#### (3) Install packages
```
yarn install
yarn android
```

#### (4) Run client
```
react-native run-android
```

## Skills
Frontend - React Native, Redux<br>
Backend - SpringBoot, Flask
<br>

## Contributors
<div>
<a href="https://github.com/hrxorxm">
  <img src="https://github.com/hrxorxm.png" width="50" height="50" >
</a>
    <a href="https://github.com/hyeju1123">
  <img src="https://github.com/hyeju1123.png" width="50" height="50" >
</a>
    <a href="https://github.com/minn12">
  <img src="https://github.com/minn12.png" width="50" height="50" >
</a>
    <a href="https://github.com/juyonLee00">
  <img src="https://github.com/juyonLee00.png" width="50" height="50" >
</a>

