package com.example.luan.adoptyourstreet.firebase;

import com.example.luan.adoptyourstreet.models.DayActivitive;
import com.example.luan.adoptyourstreet.models.Prayer;
import com.example.luan.adoptyourstreet.models.User;
import com.example.luan.adoptyourstreet.models.WeekActivitie;

/**
 * Created by luan on 6/10/16.
 */
public interface FireBaseDatabaseCallback {
    void OnPrayerAdded(Prayer newPrayer);
    void OnUserUpdated(User user);
    void showUserFromPrayer(User user,Prayer prayer);
    void OnWeekActivitie(WeekActivitie weekActivitie);
    void OnDayOfWeekActivive(DayActivitive dayActivitive);
    void StartPrayer(String idPray);
    void OnGetWeekCheck(WeekActivitie w);
    void OnGetDayCheck(DayActivitive w);
}
