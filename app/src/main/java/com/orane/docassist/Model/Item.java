package com.orane.docassist.Model;

public class Item {
    private String id,datetime;
    private String title, hid, dt, ty, amt, des, pri;
    private String askedname, geo, pubdate, link, price, speciality, fupcode, label;
    private String hlid, hlpname, hluser_geo, hlplan_months, hlplan_start_date, hlnext_renewal;
    private String hlqid, hlaskedby_name, hlasked_at, hlquery, hlfollowcode, hlstr_user_geo, hltype_lable, hlstr_qstatus;
    private String wdatetime, wdesc, wamt, wtype, pproof, country, zip, state, city, street;
    private String name, email, qid;
    private String qbanner, notes,status, info, hline,visitor_type,location,time,device_type,event_type,spec, docimg, docname, docspec, qtitle, qimage, qdesc, qtime, qlikes, qcomments;

    //--------- MyPatient List-------------------------
    public void setEventtype(String event_type) {
        this.event_type = event_type;
    }
    public String getEvent_type() {
        return event_type;
    }
    public void setVisitor_type(String visitor_type) {
        this.visitor_type = visitor_type;
    }
    public String getVisitor_type() {
        return visitor_type;
    }
    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
    public String getDevice_type() {
        return device_type;
    }
    public void setTime(String time) {
        this.time = device_type;
    }
    public String getTime() {
        return time;
    }
    public void setELocation(String location) {
        this.location = location;
    }
    public String getELocation() {
        return location;
    }
    //-------------------------------------------------------------------



    //--------- MyPatient List-------------------------
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    //---------------------------------------

    //---------Qases List-------------------------
    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSpec() {
        return spec;
    }

    public void setQbanner(String qbanner) {
        this.qbanner = qbanner;
    }

    public String getQbanner() {
        return qbanner;
    }


    public void setDocimg(String docimg) {
        this.docimg = docimg;
    }

    public String getDocimg() {
        return docimg;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDocname() {
        return docname;
    }
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setDocspec(String docspec) {
        this.docspec = docspec;
    }

    public String getDocspec() {
        return docspec;
    }

    public void setQtitle(String qtitle) {
        this.qtitle = qtitle;
    }

    public String getQtitle() {
        return qtitle;
    }

    public void setQimage(String qimage) {
        this.qimage = qimage;
    }

    public String getQimage() {
        return qimage;
    }

    public void setQdesc(String qdesc) {
        this.qdesc = qdesc;
    }

    public String getQdesc() {
        return qdesc;
    }

    public void setQtime(String qtime) {
        this.qtime = qtime;
    }

    public String getQtime() {
        return qtime;
    }

    public void setQlikes(String qlikes) {
        this.qlikes = qlikes;
    }

    public String getqlikes() {
        return qlikes;
    }

    public void setQcomments(String qcomments) {
        this.qcomments = qcomments;
    }

    public String getQcomments() {
        return qcomments;
    }
    //---------------------------------------


    //--------- MyClinic List-------------------------
    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }
/*    public void setTitle(String title) {
        this.title= title;
    }
    public String getTitle() {
        return title;
    }*/
    //--------- MyClinic List-------------------------


    //--------- Wallet Transaction List-------------------------
    public void setPproof(String pproof) {
        this.pproof = pproof;
    }

    public void setWtype(String wtype) {
        this.wtype = wtype;
    }

    public String getWtype() {
        return wtype;
    }

    public void setWdatetime(String wdatetime) {
        this.wdatetime = wdatetime;
    }

    public String getWdatetime() {
        return wdatetime;
    }

    public void setWdesc(String wdesc) {
        this.wdesc = wdesc;
    }

    public String getWdesc() {
        return wdesc;
    }

    public void setWamt(String wamt) {
        this.wamt = wamt;
    }

    public String getWamt() {
        return wamt;
    }
    //--------- Wallet Transaction List-------------------------


    //--------- Hotline Patients Queries List-------------------------
    public void setHlqid(String hlqid) {
        this.hlqid = hlqid;
    }

    public String getHlqid() {
        return hlqid;
    }

    public void setHlstr_qstatus(String hlstr_qstatus) {
        this.hlstr_qstatus = hlstr_qstatus;
    }

    public String getHlstr_qstatus() {
        return hlstr_qstatus;
    }


    //----------------------------------
    public void setHlaskedby_name(String hlaskedby_name) {
        this.hlaskedby_name = hlaskedby_name;
    }

    public String getHlaskedby_name() {
        return hlaskedby_name;
    }

    //----------------------------------
    public void setHlasked_at(String hlasked_at) {
        this.hlasked_at = hlasked_at;
    }

    public String getHlasked_at() {
        return hlasked_at;
    }

    //----------------------------------
    public void setHlquery(String hlquery) {
        this.hlquery = hlquery;
    }

    public String getHlquery() {
        return hlquery;
    }

    //----------------------------------
    public void setHlfollowcode(String hlfollowcode) {
        this.hlfollowcode = hlfollowcode;
    }

    public String getHlfollowcode() {
        return hlfollowcode;
    }

    //----------------------------------
    public void setHlstr_user_geo(String hlstr_user_geo) {
        this.hlstr_user_geo = hlstr_user_geo;
    }

    public String getHlstr_user_geo() {
        return hlstr_user_geo;
    }

    //---------------------------------
    public void setHltype_lable(String hltype_lable) {
        this.hltype_lable = hltype_lable;
    }

    public String getHltype_lable() {
        return hltype_lable;
    }
    //---------------------------------


    //--------- Hotline Patients -------------------------
    public void setHlid(String hlid) {
        this.hlid = hlid;
    }

    public String getHlid() {
        return hlid;
    }

    //----------------------------------
    public void setHlpname(String hlpname) {
        this.hlpname = hlpname;
    }

    public String getHlpname() {
        return hlpname;
    }

    //----------------------------------
    public void setHluser_geo(String hluser_geo) {
        this.hluser_geo = hluser_geo;
    }

    public String getHluser_geo() {
        return hluser_geo;
    }

    //----------------------------------
    public void setHlplan_start_date(String hlplan_start_date) {
        this.hlplan_start_date = hlplan_start_date;
    }

    public String getHlplan_start_date() {
        return hlplan_start_date;
    }

    //----------------------------------
    public void setHlplan_months(String hlplan_months) {
        this.hlplan_months = hlplan_months;
    }

    public String getHlplan_months() {
        return hlplan_months;
    }

    //----------------------------------
    public void setHlnext_renewal(String hlnext_renewal) {
        this.hlnext_renewal = hlnext_renewal;
    }

    public String getHlnext_renewal() {
        return hlnext_renewal;
    }
    //----------------------------------


    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getHline() {
        return hline;
    }

    public void setHline(String hline) {
        this.hline = hline;
    }




    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTy() {
        return ty;
    }

    public void setTy(String ty) {
        this.ty = ty;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }


    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }


    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getFupcode() {
        return fupcode;
    }

    public void setFupcode(String fupcode) {
        this.fupcode = fupcode;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAskedName() {
        return askedname;
    }

    public void seAskedName(String askedname) {
        this.askedname = askedname;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }


}
