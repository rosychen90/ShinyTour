package wpf;


public class PhotoInfo {
	public String p_id;		//��Ƭ���
	public String p_name;	//��Ƭ����
	public String p_des;	//��Ƭ����
	public String x_id;		//�������
	public Double lat;		//��Ƭ���㾭��
	public Double lng;		//��Ƭ����γ��
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
