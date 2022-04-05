#-*-coding:utf-8-*-

import urllib.request
import urllib
import json
import pandas as pd
import logging
from pandas.io.json import json_normalize

# 소켓을 사용하기 위해서는 socket을 import해야 한다.
import socket, threading;

import sys
if sys.version_info <= (2,7):
    reload(sys)
    sys.setdefaultencoding('utf-8')

def getfoodname():
    """이미지 인식해서 모델명 받아오는 코드"""
    """여기서 함수를 3개 만들어서 함수명으로 알아보기 쉽게 하는게 좋을지 아니면 getfoodname에서 nurtrients불러와서 한번에 리턴하는게 좋을지.."""


def getnutrients(foodname):
    logger.info("data")
    logger.info(foodname)
    
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

    return send_data_list


def binder(client_socket, addr):
    '''
    binder함수는 서버에서 accept가 되면 생성되는 socket 인스턴스를 통해 
    client로 부터 데이터를 받으면 echo형태로 재송신하는 메소드이다.
    '''
    # 커넥션이 되면 접속 주소가 나온다.
    print('Connected by', addr)
    try:
        # 접속 상태에서는 클라이언트로 부터 받을 데이터를 무한 대기한다.
        # 만약 접속이 끊기게 된다면 except가 발생해서 접속이 끊기게 된다.
        while True:
            # socket의 recv함수는 연결된 소켓으로부터 데이터를 받을 대기하는 함수입니다. 최초 4바이트를 대기합니다.
            data = client_socket.recv(4)
            # 최초 4바이트는 전송할 데이터의 크기이다. 그 크기는 little big 엔디언으로 byte에서 int형식으로 변환한다.
            # C#의 BitConverter는 big엔디언으로 처리된다.
            length = int.from_bytes(data, "little")
            # 다시 데이터를 수신한다.
            data = client_socket.recv(length)
            # 수신된 데이터를 str형식으로 decode한다.
            msg = data.decode()
            # 수신된 메시지를 콘솔에 출력한다.
            print('Received from', addr, msg)
            # 수신된 메시지 앞에 「echo:」 라는 메시지를 붙힌다.
            msg = "echo : " + msg
            # 바이너리(byte)형식으로 변환한다.
            data = msg.encode()
            # 바이너리의 데이터 사이즈를 구한다.
            length = len(data)
            # 데이터 사이즈를 little 엔디언 형식으로 byte로 변환한 다음 전송한다.
            client_socket.sendall(length.to_bytes(4, byteorder='little'))
            # 데이터를 클라이언트로 전송한다.
            client_socket.sendall(data)
    except:
        # 접속이 끊기면 except가 발생한다.
        print("except : " , addr)
    finally:
        # 접속이 끊기면 socket 리소스를 닫는다.
        client_socket.close()


def __get_logger():
    __logger = logging.getLogger('logger')

    # 로그 포멧 정의
    formatter = logging.Formatter(
        'BATCH##AWSBATCH##%(levelname)s##%(asctime)s##%(message)s >> @@file::%(filename)s@@line::%(lineno)s')
    # 스트림 핸들러 정의
    stream_handler = logging.StreamHandler()
    # 각 핸들러에 포멧 지정
    stream_handler.setFormatter(formatter)
    # 로거 인스턴스에 핸들러 삽입
    __logger.addHandler(stream_handler)
    # 로그 레벨 정의
    __logger.setLevel(logging.DEBUG)

    return __logger


if __name__ == "__main__":
    print("start")
    logger = __get_logger()
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 소켓 레벨과 데이터 형태를 설정한다.
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    # 서버는 복수 ip를 사용하는 pc의 경우는 ip를 지정하고 그렇지 않으면 None이 아닌 ''로 설정한다.
    # 포트는 pc내에서 비어있는 포트를 사용한다. cmd에서 netstat -an | find "LISTEN"으로 확인할 수 있다.
    server_socket.bind(('', 9000))
    # server 설정이 완료되면 listen를 시작한다.
    server_socket.listen()

    try:
        # 서버는 여러 클라이언트를 상대하기 때문에 무한 루프를 사용한다.
        while True:
            print("OK")
            # client로 접속이 발생하면 accept가 발생한다.
            # 그럼 client 소켓과 addr(주소)를 튜플로 받는다.
            client_socket, addr = server_socket.accept()
            th = threading.Thread(target=binder, args = (client_socket, addr))
            # 쓰레드를 이용해서 client 접속 대기를 만들고 다시 accept로 넘어가서 다른 client를 대기한다.
            th.start()
    except:
        print("server")
    finally:
            # 에러가 발생하면 서버 소켓을 닫는다.
        server_socket.close()
