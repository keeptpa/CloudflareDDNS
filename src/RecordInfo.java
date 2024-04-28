import java.util.ArrayList;
import java.util.Date;

class Meta{
    public boolean auto_added;
    public String source;
}

class Result{
    public String content;
    public String name;
    public boolean proxied;
    public String type;
    public String comment;
    public Date created_on;
    public String id;
    public boolean locked;
    public Meta meta;
    public Date modified_on;
    public boolean proxiable;
    public ArrayList<String> tags;
    public int ttl;
    public String zone_id;
    public String zone_name;
}

 class ResultInfo{
    public int count;
    public int page;
    public int per_page;
    public int total_count;
}

public class RecordInfo{
    public ArrayList<Object> errors;
    public ArrayList<Object> messages;
    public boolean success;
    public ArrayList<Result> result;
    public ResultInfo result_info;
}

