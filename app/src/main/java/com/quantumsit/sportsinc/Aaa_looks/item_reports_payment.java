package com.quantumsit.sportsinc.Aaa_looks;

/**
 * Created by mona_ on 12/28/2017.
 */

public class item_reports_payment {

    String course_name, creation_date, due_date;
    int payment, status; //status: 0 not_payed, 1 payed;

    public item_reports_payment(String course_name, String creation_date, int payment, String due_date, int status) {
        this.course_name = course_name;
        this.creation_date = creation_date;
        this.payment = payment;
        this.due_date = due_date;
        this.status = status;
    }
}
