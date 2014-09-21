package wpf;


public class PhotoInfo {
	public String p_id;		//相片编号
	public String p_name;	//相片名称
	public String p_des;	//相片描述
	public String x_id;		//相册名称
	public Double lat;		//相片拍摄经度
	public Double lng;		//相片拍摄纬度
	public PhotoInfo(String pId, String pName, String pDes, String xId,Double lat, Double lng) {
		super();
		this.p_id = pId;
		this.p_name = pName;
		this.p_des = pDes;
		this.x_id = xId;
		this.lat = lat;
		this.lng = lng;
	}
}
