import java.awt.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.util.*;

public class Bee extends JPanel {
    
    private BufferedImage bufImage; //������ʾ�Ļ�����ͼ��
    private BufferedImage originalBufImage; //ԭʼ������ͼ��
    private Graphics2D bufImageG; //������ͼ���ͼ�λ���    
	private static Toolkit tk = Toolkit.getDefaultToolkit();
    /**�۷䵱ǰ������*/
	private int id;
	private Image img;//�۷��ͼƬ
	private int posX;
	private int posY;
	private boolean isAlive;
	private FlyingStatus fs;
	private int[] nextX;
	private int[] nextY;  
	String isCatched;
	
	/**�۷�ķ����ٶ�*/
	private int speed = 18;
	//private int speed = 27;//@YQԭ���Ĵ���Ϊ18
	
	/**�۷䵱ǰ���з������һ�η�����һ�η�����������ת��Χ*/
	double angle,oldAngle;
	
	/**�۷�Ĺ��캯��
	* @param id �۷��ID
	* @param x,y �۷��������ĵ�����
	* @param angle �۷����ʱ�ĽǶ�
	* @param img �۷��ͼƬ
	*/
	public Bee(int id,int x, int y, double angle,boolean isAlive,Image img){
		this.id = id;
		this.posX = x;
		this.posY = y;
		this.img = img;
		this.angle = angle;
		this.isAlive = isAlive;
		fs = new FlyingStatus(id,x,y,angle,isAlive,0);
		if(fs.isAlive==true){
			BeeFarming.update(fs);//�������򱨸��Լ���״̬
		}
		setOpaque(false);//����JPanel͸����ֻ��ʾͼƬ������ʾ�װ�
		//����JPanel�Ĵ�С������Ҫ�����򻭲��Ͽ������κζ���
		setSize(img.getWidth(null),img.getHeight(null));
		//����JPanel�ڻ����е�λ��
		setLocation(posX-img.getWidth(null)/2,posY-img.getHeight(null)/2);//30������ϵ��΢�Ĳ��
		//����ͼ�񻺳��������治��Ҫ�˽�ϸ�ڣ�
		originalBufImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);     //����ԭʼ������ͼ��
		bufImage = originalBufImage;
        bufImageG = bufImage.createGraphics(); //����bufImage��ͼ�λ���
        bufImageG.drawImage(img, 0, 0, this); //����Դͼ�����ݵ�������ͼ����
		ratoteImage(angle);
	}
	
    /**��ת�۷�����ķ���
	* @param a �۷�������ת��ĽǶȣ���Χ��0-360��X �������ϵĵ��� Y ��������ת
	*/
    public void ratoteImage(double a) {
        if (bufImage == null)
            return; //���bufImageΪ����ֱ�ӷ���
        BufferedImage filteredBufImage =new BufferedImage(img.getWidth(this) ,img.getHeight(this),BufferedImage.TYPE_INT_ARGB); //���˺��ͼ��
        AffineTransform transform = new AffineTransform(); //����任����
        transform.rotate(Math.toRadians(a),img.getWidth(this)/2,img.getHeight(this)/2);
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);//��������任��������            
        imageOp.filter(originalBufImage, filteredBufImage);//����ͼ��Ŀ��ͼ����filteredBufImage
        bufImage = filteredBufImage; //��������ʾ�Ļ�����ͼ��ָ����˺��ͼ��
        repaint(); //�ػ����
		oldAngle = angle;//��ת������һ�νǶȼ�¼�������ж���Ұ��Χ
		angle = a;
		//System.out.println("��ת�Ƕȣ�"+angle);
    }
        
    /**����������paintComponent()����*/
    public void paint(Graphics g) {
        super.paintComponent(g);
        if (bufImage != null&& isAlive == true) {
            Graphics2D g2 = (Graphics2D) g;
			Color c = g2.getColor();
			Font f = g2.getFont();
            g2.drawImage(bufImage,0,0,this);    //����ͼƬ
			//g2.setFont(new Font("Arial",Font.PLAIN,10));
			//g2.drawString("x:"+posX, 10, 10); 
			//g2.drawString("y:"+posY, 10, 26); 
			g2.setColor(c);
			g2.setFont(f);
        }
    } 

	/**�۷���������������ȷ����һ�������ڵķ���·��
	*/
	public void search(){
		String strVision = BeeFarming.search(id);
		//�������*Ϊ�׵��ַ��������������˱ߣ����������˳ʱ����ת90�����ڵĽǶ�
		if(strVision.indexOf('*')==0)
		{			
			Random ra = new Random();
			angle += ra.nextInt(90);
			ratoteImage(angle);
		}
		//�����������Ϣ�����ӡ����
		if(strVision.length()>0)
			System.out.println(strVision);	
		setXYs(0);
	}
	
	/**���ݵ�ǰ���нǶ�ȷ����һ�η��е������������,�����nextX[]��nextY[]��
     * @param start ���ĸ�ʱ�̿�ʼ���У������۷����Ԥ֪��һ�ַ����Ƿ���꣬������ǰ�趨�������꣬���������
	 *  ȱʡΪ0�������۷�����ۺ�����һ���������ڵ���ǰ����ͣ��ԭ��
	*/
	public void setXYs(int start){
		//nextX[]��nextY[]�Ǵ����һ���ȶ����е��������
		nextX = new int[9];
		nextY = new int[9];
		int deltaX = (int)(speed * (9-start)/9.0 * Math.cos(Math.toRadians(angle)));
		int deltaY = (int)(speed * (9-start)/9.0 * Math.sin(Math.toRadians(angle)));
		//������õ����λ�ƿ���ʵ��С�Ƕȱ仯��λ��
		int dx_sum = 0;
		int dy_sum = 0;
		//��������ڻ�δ���껨�۲ɹ⣬���м�����start��ʾ�ӵڼ��ַ�������ֹͣ���۲���������
		for(int i=start;i<9;i++){
			nextX[i] = (int)(deltaX * (i+1)/ 9.0)-dx_sum;
			nextY[i] = (int)(deltaY * (i+1)/ 9.0)-dy_sum;
			dx_sum += nextX[i];
			dy_sum += nextY[i];
		}
	}
	
	/**�۷䰴�ռ���õ�һϵ��λ�ñ����ȶ����У��ڷ����ڼ䲻����罻��
	* @param i Ŀǰ���е��ڼ����Ѿ�ȷ����λ��
	*/
	public void flying(int i){
		//x,y���Ƿ��л��ۣ�����оͲ��ۣ�������һʱ��ͣ��(��next[i+1]=0)
		//�۷�ɵ�һ���ʱ�������˻����͵�ͣ����ֱ�����걾�η�������
		if(id!=9&&fs.isAlive==true&&BeeFarming.pickFlowerHoney(id)==1&&i<9){
			//System.out.println((i)+": set 0");
			nextX[i]=0;
			nextY[i]=0;
			//����ʱ��ת��������з���Ĳ�ȷ����
			//angle+=30;
			//ratoteImage(angle);
		}
		//ѯ��beefarming���Լ���׽ס��׽ס�����ˣ���׽ס-������׽ס-Ѱ���µ�Ŀ��
		posX += nextX[i];
		posY += nextY[i];
		if(id==9){
			BeeFarming.killBee(9);
		}
		isAlive = getlive();
		fs = new FlyingStatus(id,posX,posY,angle,isAlive,0);
		if(fs.isAlive==true)
		BeeFarming.update(fs);//�������򱨸��Լ���״̬
		setLocation(posX-img.getWidth(null)/2,posY-img.getHeight(null)/2);
	}
	
	public boolean getlive(){
	    isAlive=fs.isAlive;
		return isAlive;
	}
	
	public boolean live(boolean isAlive){
       
		if(isAlive=false)
		{
			setVisible(false);
			return false;
		}
		repaint();
		return true;
	}
	
}