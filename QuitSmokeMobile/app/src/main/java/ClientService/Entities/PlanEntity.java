package ClientService.Entities;

import android.support.v4.widget.SlidingPaneLayout;

public class PlanEntity {
    private String uid;
    private String title;
    private String quit_date;
    private int no_of_smoke_week1;
    private int no_of_smoke_week2;
    private int no_of_smoke_week3;
    private int no_of_smoke_week4;
    private String create_date;
    private String status;

    public PlanEntity() {}

    public PlanEntity(String uid, String title, String quit_date, int no_of_smoke_week1, int no_of_smoke_week2, int no_of_smoke_week3, int no_of_smoke_week4, String create_date, String status) {
        this.uid = uid;
        this.title = title;
        this.quit_date = quit_date;
        this.no_of_smoke_week1 = no_of_smoke_week1;
        this.no_of_smoke_week2 = no_of_smoke_week2;
        this.no_of_smoke_week3 = no_of_smoke_week3;
        this.no_of_smoke_week4 = no_of_smoke_week4;
        this.create_date = create_date;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuit_date() {
        return quit_date;
    }

    public void setQuit_date(String quit_date) {
        this.quit_date = quit_date;
    }

    public int getNo_of_smoke_week1() {
        return no_of_smoke_week1;
    }

    public void setNo_of_smoke_week1(int no_of_smoke_week1) {
        this.no_of_smoke_week1 = no_of_smoke_week1;
    }

    public int getNo_of_smoke_week2() {
        return no_of_smoke_week2;
    }

    public void setNo_of_smoke_week2(int no_of_smoke_week2) {
        this.no_of_smoke_week2 = no_of_smoke_week2;
    }

    public int getNo_of_smoke_week3() {
        return no_of_smoke_week3;
    }

    public void setNo_of_smoke_week3(int no_of_smoke_week3) {
        this.no_of_smoke_week3 = no_of_smoke_week3;
    }

    public int getNo_of_smoke_week4() {
        return no_of_smoke_week4;
    }

    public void setNo_of_smoke_week4(int no_of_smoke_week4) {
        this.no_of_smoke_week4 = no_of_smoke_week4;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
