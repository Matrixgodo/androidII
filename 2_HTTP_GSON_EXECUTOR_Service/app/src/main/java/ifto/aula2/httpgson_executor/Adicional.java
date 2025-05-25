package ifto.aula2.httpgson_executor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Adicional {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;

    /**
     * No args constructor for use in serialization
     *
     */
    public Adicional() {
    }

    public Adicional(String id, String email) {
        super();
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
