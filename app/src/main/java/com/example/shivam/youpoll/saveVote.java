package com.example.shivam.youpoll;

public interface saveVote {
    public void saveVote(Integer x, String CHOICE);

    public void removePreviousVote(Integer x, String Choice);
}
