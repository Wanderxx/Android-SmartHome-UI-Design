
public class FlyingStatus{
	int id;
	int x;
	int y;
	double angle;
	boolean isAlive;
	int status;
	
	/**����״̬�Ĺ��캯��
	* @param id �۷��id
	* @param x,y ��ǰ������
	* @param angle �۷���еĽǶȣ�0-360�ȣ�
	* @param status ״̬
	*/
	public FlyingStatus(int id,int x,int y,double angle,boolean isAlive,int status){
		this.id = id;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.isAlive = isAlive;
		this.status = status;
	}
	public boolean getisAlive(){
		return isAlive;
	}
		
}