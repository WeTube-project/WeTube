package com.andkjyk.wetube_v0.Model;

// chatting 기능, 싱크 기능에서 socket.io로 주고받는 방 정보, RoomItem과 별개이다.
public class RoomData {     // 채팅, 동기화 기능을 위해 서버로 보낼 방 정보를 담는 클래스
    private String userName;
    private String roomCode;

    public RoomData(String userName, String roomCode) {
        this.userName = userName;
        this.roomCode = roomCode;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}
