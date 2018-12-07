package bean;


public class AllEventShow {
    String Time;
    String Place;
    String Description;

    public AllEventShow(String time,String place,String description){
        Time = time;
        Place = place;
        Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
