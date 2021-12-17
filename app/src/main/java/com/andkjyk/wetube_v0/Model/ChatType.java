package com.andkjyk.wetube_v0.Model;

public class ChatType {     // 메세지의 종류를 구분하는 클래스
    public static final int LEFT_MESSAGE = 0;   // 자신 외의 사용자가 전송한 메세지는 좌측 말풍선으로 띄워지도록 타입 설정
    public static final int CENTER_MESSAGE = 1; // 입장/퇴장 메세지는 중앙에 띄워지도록
    public static final int RIGHT_MESSAGE = 2;  // 자신이 보낸 메세지는 우측 말풍선으로 띄워지도록
}
