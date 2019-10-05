package friendlyitsolution.com.firebase_db;

public class DataModel
{
    String id,msg,time,date,read;
    DataModel(String id,String msg,String time,String date,String read)
    {
        this.date=date;
        this.time=time;
        this.id=id;
        this.msg=msg;
        this.read=read;
    }
}
