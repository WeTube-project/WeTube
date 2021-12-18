package com.andkjyk.wetube_v0;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;

public class VideoChatFragment extends Fragment implements JitsiMeetActivityInterface {

    private JitsiMeetView jitsiMeetView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_chat, container, false);

        FrameLayout jitsiView = view.findViewById(R.id.jitsi_frame);
        jitsiMeetView = new JitsiMeetView(getActivity());
        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setRoom("https://meet.jit.si/a7d8sd")
                .setAudioMuted(false)
                .setVideoMuted(false)
                .setAudioOnly(false)
                .setWelcomePageEnabled(false)
                .build();

        jitsiMeetView.join(options);

        jitsiView.addView(jitsiMeetView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        return view;

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void init(View view){

    }

    @Override
    public int checkPermission(String s, int i, int i1) {
        return 0;
    }

    @Override
    public int checkSelfPermission(String s) {
        return 0;
    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {

    }
}
