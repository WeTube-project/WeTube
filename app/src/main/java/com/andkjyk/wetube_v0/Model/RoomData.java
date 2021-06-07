package com.andkjyk.wetube_v0.Model;

// chatting 기능 socket.io로 주고받는 방 정보, RoomItem과 별개이다.
public class RoomData {
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
