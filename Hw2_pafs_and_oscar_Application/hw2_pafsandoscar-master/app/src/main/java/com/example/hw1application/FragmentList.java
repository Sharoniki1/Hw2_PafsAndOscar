package com.example.hw1application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

public class FragmentList extends Fragment {
    private MaterialButton[] list_BTN_scores;
    private LeaderBoard leaderBoard;
    private CallBack_ScoreProtocol callBack_scoreProtocol;
    private CallBack_TimingProtocol callBack_timingProtocol;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_list, container, false);

        findViews(view);
        initViews();
        read();
        leaderBoard = new LeaderBoard();

        callBack_timingProtocol.ready();

        return view;
    }
    public void refreshUI() {
        for (int i = 0; i < leaderBoard.getScores().size(); i++)
            list_BTN_scores[i].setText(leaderBoard.getScores().get(i).toString());
    }

    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }

    private void initViews() {

        for(int i = 0; i < list_BTN_scores.length; i++) {
            int finalI = i;
            list_BTN_scores[i].setOnClickListener(view -> {
                scoreClicked(callBack_scoreProtocol, finalI);
            });
        }
    }

    private void scoreClicked(CallBack_ScoreProtocol callBack_scoreProtocol, int i) {
        if(callBack_scoreProtocol != null) {
            callBack_scoreProtocol.score(leaderBoard.getScores().get(i));
        }
    }

    private void findViews(View view) {
        list_BTN_scores = new MaterialButton[] {
                view.findViewById(R.id.fragmentlist_BTN_button1),
                view.findViewById(R.id.fragmentlist_BTN_button2),
                view.findViewById(R.id.fragmentlist_BTN_button3),
                view.findViewById(R.id.fragmentlist_BTN_button4),
                view.findViewById(R.id.fragmentlist_BTN_button5),
                view.findViewById(R.id.fragmentlist_BTN_button6),
                view.findViewById(R.id.fragmentlist_BTN_button7),
                view.findViewById(R.id.fragmentlist_BTN_button8),
                view.findViewById(R.id.fragmentlist_BTN_button9),
                view.findViewById(R.id.fragmentlist_BTN_button10)

        };
    }

    public void setCallBack_scoreProtocol(CallBack_ScoreProtocol callBack_scoreProtocol) {
        this.callBack_scoreProtocol = callBack_scoreProtocol;
    }
    public void setCallBack_timingProtocol(CallBack_TimingProtocol callBack_timingProtocol) {
        this.callBack_timingProtocol = callBack_timingProtocol;
    }

    public void read(){
        String myJsonString= SPV3.getInstance().getString("List", null);
        leaderBoard = new Gson().fromJson(myJsonString, LeaderBoard.class);
        if(leaderBoard == null)
            leaderBoard = new LeaderBoard();
    }

    public void write(){
        SPV3.getInstance().putString("List", new Gson().toJson(leaderBoard));
    }
}
