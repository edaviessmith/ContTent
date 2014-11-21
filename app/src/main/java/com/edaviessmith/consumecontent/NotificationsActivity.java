package com.edaviessmith.consumecontent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.edaviessmith.consumecontent.data.Alarm;
import com.edaviessmith.consumecontent.data.Notification;
import com.edaviessmith.consumecontent.db.NotificationORM;
import com.edaviessmith.consumecontent.util.Var;
import com.edaviessmith.consumecontent.view.AlarmDialog;
import com.edaviessmith.consumecontent.view.Fab;

import java.util.List;


public class NotificationsActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    String TAG = "NotificationsActivity";

    Toolbar toolbar;
    List<Notification> notifications;
    Notification editNotification;
    ListView notification_lv, alarm_lv;
    NotificationAdapter notificationAdapter;
    AlarmAdapter alarmAdapter;

    Fab add_fab, addAlarm_fab, save_fab;
    View alarm_v, allNotifications_v, mobileNotifications_v, vibrations_v;
    SwitchCompat allNotifications_sw, mobileNotifications_sw, vibrations_sw;
    boolean isAllNotificationsEnabled, isMobileNotificationsEnabled, isVibrationsEnabled;
    EditText notificationName_tv;

    public static final int NOTIFICATIONS_LIST = 0;
    public static final int ALARMS_LIST = 1;

    private int listType = NOTIFICATIONS_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        notifications = NotificationORM.getNotifications(this);
        editNotification = new Notification();

        //editNotification.setType(Var.NOTIFICATION_SLEEP); //TODO testing adding first one (this should be added in the DB

        isAllNotificationsEnabled = Var.getBoolPreference(this, Var.PREF_ALL_NOTIFICATIONS);
        isMobileNotificationsEnabled = Var.getBoolPreference(this, Var.PREF_MOBILE_NOTIFICATIONS);
        isVibrationsEnabled = Var.getBoolPreference(this, Var.PREF_VIBRATIONS);

        View header = getLayoutInflater().inflate(R.layout.header_notifications, null, false);
        View alarmHeader = getLayoutInflater().inflate(R.layout.header_alarms, null, false);


        alarm_v = findViewById(R.id.alarm_v);
        save_fab = (Fab) findViewById(R.id.save_fab);

        alarm_lv = (ListView) findViewById(R.id.alarm_lv);
        alarm_lv.addHeaderView(alarmHeader, null, false);
        addAlarm_fab = (Fab) alarmHeader.findViewById(R.id.add_alarm_fab);

        notification_lv = (ListView) findViewById(R.id.notification_lv);
        notification_lv.addHeaderView(header, null, false);

        add_fab = (Fab) header.findViewById(R.id.add_fab);
        allNotifications_v = header.findViewById(R.id.all_notifications_v);
        allNotifications_sw = (SwitchCompat) header.findViewById(R.id.all_notifications_sw);
        allNotifications_sw.setChecked(isAllNotificationsEnabled);
        mobileNotifications_v = header.findViewById(R.id.mobile_notifications_v);
        mobileNotifications_sw = (SwitchCompat) header.findViewById(R.id.mobile_notifications_sw);
        mobileNotifications_sw.setChecked(isMobileNotificationsEnabled);
        vibrations_v = header.findViewById(R.id.vibrations_v);
        vibrations_sw = (SwitchCompat) header.findViewById(R.id.vibrations_sw);
        vibrations_sw.setChecked(isVibrationsEnabled);

        notificationName_tv = (EditText) alarmHeader.findViewById(R.id.notification_name_tv);

        save_fab.setOnClickListener(this);
        addAlarm_fab.setOnClickListener(this);

        add_fab.setOnClickListener(this);
        allNotifications_v.setOnClickListener(this);
        mobileNotifications_v.setOnClickListener(this);
        vibrations_v.setOnClickListener(this);

        notificationAdapter = new NotificationAdapter(this);
        notification_lv.setAdapter(notificationAdapter);
        notification_lv.setOnItemClickListener(this);

        alarmAdapter = new AlarmAdapter(this);
        alarm_lv.setAdapter(alarmAdapter);

    }


    public void toggleList(int listType) {
        this.listType = listType;

        alarm_v.setVisibility((listType == ALARMS_LIST) ? View.VISIBLE: View.GONE);

        if(listType == NOTIFICATIONS_LIST) {
            notificationAdapter.notifyDataSetChanged();
        }
        if(listType == ALARMS_LIST) {
            notificationName_tv.setText(editNotification.getName());
        }
    }

    public void addAlarm(Alarm alarm) {
        if(!editNotification.getAlarms().contains(alarm)) editNotification.getAlarms().add(alarm);
        alarmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editNotification = notifications.get(position - 1);
        toggleList(ALARMS_LIST);
    }

    public class NotificationAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public NotificationAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return notifications.size();
        }

        @Override
        public Notification getItem(int position) {
            return notifications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_notification, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Notification notification = getItem(position);
            if(notification.getType() == Var.NOTIFICATION_ALARM) holder.icon_iv.setImageResource(R.drawable.ic_alarm_grey600_36dp);
            if(notification.getType() == Var.NOTIFICATION_SLEEP) holder.icon_iv.setImageResource(R.drawable.ic_alarm_off_grey600_36dp);
            holder.name_tv.setText(notification.getName());

            return convertView;

        }

        class ViewHolder {
            ImageView icon_iv;
            TextView name_tv;

            public ViewHolder(View view) {
                icon_iv = (ImageView) view.findViewById(R.id.icon_iv);
                name_tv = (TextView) view.findViewById(R.id.name_tv);
            }
        }


    }

    public class AlarmAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        Context context;

        public AlarmAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return editNotification.getAlarms().size();
        }

        @Override
        public Alarm getItem(int position) {
            return editNotification.getAlarms().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_alarm, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Alarm alarm = getItem(position);


            holder.time_tv.setText(Var.getAlarmText(alarm));
            holder.time_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlarmDialog(NotificationsActivity.this, alarm);
                }
            });

            holder.onlyWifi_iv.setVisibility(alarm.isOnlyWifi()? View.VISIBLE: View.GONE);

            if(alarm.getDays().size() == 7) { //Make sure the week is there
                setDayTb(alarm, holder.sun_tb);
                setDayTb(alarm, holder.mon_tb);
                setDayTb(alarm, holder.tue_tb);
                setDayTb(alarm, holder.wed_tb);
                setDayTb(alarm, holder.thu_tb);
                setDayTb(alarm, holder.fri_tb);
                setDayTb(alarm, holder.sat_tb);
            }

            View.OnClickListener dayOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int day = getDayIndex((ToggleButton) v);
                    List<Integer> days = getItem(position).getDays();
                    days.set(day, (days.get(day) == 1 ? 0 : 1));   //Toggle opposite
                }
            };

            holder.sun_tb.setOnClickListener(dayOnClickListener);
            holder.mon_tb.setOnClickListener(dayOnClickListener);
            holder.tue_tb.setOnClickListener(dayOnClickListener);
            holder.wed_tb.setOnClickListener(dayOnClickListener);
            holder.thu_tb.setOnClickListener(dayOnClickListener);
            holder.fri_tb.setOnClickListener(dayOnClickListener);
            holder.sat_tb.setOnClickListener(dayOnClickListener);

            holder.enabled_sw.setChecked(alarm.isEnabled());
            holder.nextAlarm_tv.setText(alarm.isEnabled()? Var.getNextAlarmTime(alarm): "disabled");


            //TODO create util to get next alarm time (will be needed for service soon)

            holder.delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editNotification.getAlarms().remove(alarm);
                    AlarmAdapter.this.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView time_tv;
            ImageView delete_iv;
            ToggleButton sun_tb, mon_tb, tue_tb, wed_tb, thu_tb, fri_tb, sat_tb;
            SwitchCompat enabled_sw;
            TextView nextAlarm_tv;
            ImageView onlyWifi_iv;

            public ViewHolder(View view) {
                time_tv = (TextView) view.findViewById(R.id.time_tv);
                delete_iv = (ImageView) view.findViewById(R.id.delete_iv);
                sun_tb = (ToggleButton) view.findViewById(R.id.sun_tb);
                mon_tb = (ToggleButton) view.findViewById(R.id.mon_tb);
                tue_tb = (ToggleButton) view.findViewById(R.id.tue_tb);
                wed_tb = (ToggleButton) view.findViewById(R.id.wed_tb);
                thu_tb = (ToggleButton) view.findViewById(R.id.thu_tb);
                fri_tb = (ToggleButton) view.findViewById(R.id.fri_tb);
                sat_tb = (ToggleButton) view.findViewById(R.id.sat_tb);
                enabled_sw = (SwitchCompat) view.findViewById(R.id.enabled_sw);
                nextAlarm_tv = (TextView) view.findViewById(R.id.next_alarm_tv);
                onlyWifi_iv = (ImageView) view.findViewById(R.id.only_wifi_iv);
            }
        }

        private void setDayTb(Alarm alarm, ToggleButton tb) {
            tb.setChecked(alarm.getDays().get(getDayIndex(tb)) == 1);
        }

        private int getDayIndex(ToggleButton tb) {
            if(tb.getId() == R.id.sun_tb) return 0;
            if(tb.getId() == R.id.mon_tb) return 1;
            if(tb.getId() == R.id.tue_tb) return 2;
            if(tb.getId() == R.id.wed_tb) return 3;
            if(tb.getId() == R.id.thu_tb) return 4;
            if(tb.getId() == R.id.fri_tb) return 5;
            if(tb.getId() == R.id.sat_tb) return 6;
            return 0;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(listType != NOTIFICATIONS_LIST) {
            toggleList(NOTIFICATIONS_LIST);
            return;
        }
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {


        if(save_fab == v) {
            editNotification.setName(notificationName_tv.getText().toString().trim());
            //TODO: Make sure data works before trying to save
            NotificationORM.saveNotification(this, editNotification);

            if(!notifications.contains(editNotification)) notifications.add(editNotification);
            alarmAdapter.notifyDataSetChanged();

            toggleList(NOTIFICATIONS_LIST);
        }

        if(addAlarm_fab == v) {

            new AlarmDialog(this, new Alarm(editNotification.getType()));
        }

        if(add_fab == v) {
            editNotification = new Notification();
            toggleList(ALARMS_LIST);
        }
        if(allNotifications_v == v) {
            isAllNotificationsEnabled = !isAllNotificationsEnabled;
            Var.setBoolPreference(this, Var.PREF_ALL_NOTIFICATIONS, isAllNotificationsEnabled);
            allNotifications_sw.setChecked(isAllNotificationsEnabled);
        }
        if(mobileNotifications_v == v) {
            isMobileNotificationsEnabled = !isMobileNotificationsEnabled;
            Var.setBoolPreference(this, Var.PREF_MOBILE_NOTIFICATIONS, isMobileNotificationsEnabled);
            mobileNotifications_sw.setChecked(isMobileNotificationsEnabled);
        }
        if(vibrations_v == v) {
            isVibrationsEnabled = !isVibrationsEnabled;
            Var.setBoolPreference(this, Var.PREF_VIBRATIONS, isVibrationsEnabled);
            vibrations_sw.setChecked(isVibrationsEnabled);
        }
    }
}