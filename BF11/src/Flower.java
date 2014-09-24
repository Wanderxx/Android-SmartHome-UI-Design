import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Point;
import javax.swing.JPanel;

public class Flower extends JPanel {
	Image img;//����ͼƬ
	BufferedImage bufImage; //������ʾ�Ļ�����ͼ��
	BufferedImage originalBufImage; //ԭʼ������ͼ��
	Graphics2D bufImageG; //������ͼ���ͼ�λ���    
	/**��������*/
	private int posX;
	private int posY;
	private int volumn;
	/**��������ʱ��*/
	private int time;
	/**�����ڵ����*/
	private BackGroundJP pGround;
	
	/**���Ĺ��캯��
	* @param x,y �����ĵ�����
	* @param volumn ��������
	* @param img ����ͼ��
	*/
	public Flower(int time,int x, int y, int volumn,Image img,BackGroundJP pGround){
		this.pGround = pGround;
	    this.time = time;
		this.posX = x;
		this.posY = y;
		this.img = img;
		this.volumn = volumn;
		setOpaque(false);//����JPanel͸����ֻ��ʾͼƬ������ʾ�װ�
		//����JPanel�Ĵ�С������Ҫ�����򻭲��Ͽ������κζ���
		setSize(64,64);
		//����JPanel�ڻ����е�λ��
		setLocation(posX-32,posY-32);
		originalBufImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);     //����ԭʼ������ͼ��
		bufImage = originalBufImage;
	    bufImageG = bufImage.createGraphics(); //����bufImage��ͼ�λ���
	    bufImageG.drawImage(img,(56-img.getWidth(null))/2,(56-img.getHeight(null))/2, this); //����Դͼ�����ݵ�������ͼ����
	}
	
	/**����������paintComponent()����*/
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		//ֻ�л�����������0���Ż��ƻ�
		if (bufImage != null&&volumn>0) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(bufImage,3,3,this);    //����ͼƬ
			Color c = g2.getColor();
			Font f = g2.getFont();
			
			//g2.setColor(new Color(255,255,255));
			//g2.drawRect(0,0,63,63);
			g2.setColor(new Color(255,255,0));
			g2.fillOval(28,28,8,8);	
			//g2.setFont(new Font("Arial",Font.PLAIN,10));
			//g2.drawString(volumn+"("+posX+","+posY+")", 5, 12); 
			g2.setColor(c);
			g2.setFont(f);
		}      
	} 
	
	/**ÿ�α��۷����ĵķ��ۣ��������Ϊ0�����Ͳ���ʾ
	* @param sub �۷�һ�β��۵�����
	* @return �Ƿ�����ٲ��ۣ�true-�����ۣ�false-���һ�������ˣ�
	*/
	public boolean consume(int sub){	
		volumn -= sub;
		if(volumn<=0)
		{
			setVisible(false);
			return false;
		}
		repaint();
		return true;
	}
    
	/**���ʣ�໨�۵�����
	* @return ���ػ��۵���
	*/
	public int getVolumn(){
		return volumn;
	}
	
	/**��û���λ��
	* @return ���ػ��۵�����
	*/
	public Point getPosition(){
		return new Point(posX,posY);
	}
	
	/**��û�������ʱ��
	* @return ���ػ�������
	*/
	public int getTime(){
		return time;
	}
	
	/**������
	* @return ����pGround
	*/
	public BackGroundJP getPGround(){
		return pGround;
	}
}


