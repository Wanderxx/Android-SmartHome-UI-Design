import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
public class BeeFarming extends JFrame{
	private Image imgBee;//�������۷��ͼƬ
	private Image imgBee1,imgFlw;
	//private Image imgFlw[3];
	private Image imgBee2;
	private static Image imgBG;
	private Bee bee;
	private int angle_damp = 0;
	private boolean flag = false;
	private boolean begin = true;
    
	//����ʹ��protected��Ϊ��������ܱ���BackGroundJP����
	protected static int time = 0;
    protected static int count = 3;	//�۷�����
	protected static int countflower = 20;//��������������δ������
	protected static int Beegoal = 0;//����۷���Զһ��Ļ������ 
	protected static String game = "";
	//private final static int[] SPEED = {6,6,6,6,6,6,6,15,15,12};
	private final static int[] RANGE = {50,50,50,50,50,50,50,50,50,100};
	//private final static int[] SIGHT = {120,120,120,120,120,120,120,120,120,150}; //����180�����Բ�����
	private final static int BG_WIDTH=800;
	private final static int BG_HEIGHT=600;
	private static Bee[] bees = new Bee[10];//�۷�������飬���ÿֻ�۷����ľ��
	private static Hornet hornet;
	private static FlyingStatus[] status = new FlyingStatus[10];//ÿֻ�۷䵱ǰ����״̬���
	//private static String[] messages = new String[10];//�۷����Ϣ���䣬ÿ���۷�һ���������໥ͨ��
	protected static int totalHoney = 0;
	private static ArrayList<Flower> flowers = new ArrayList<Flower>();
    Music music = new Music("flourish.mid");
	
	private static Flower[] flws = new Flower[20];
	/**�๹�캯�����ڳ�ʼ�������Ϸ�еĶ�������*/
	public BeeFarming(){
        setTitle("BeeFarming Game"); //���ø��๹�캯��               
	 	imgBee = getToolkit().getImage("images/bee.png");
		imgBee2 = getToolkit().getImage("images/bee2.png");
		imgBG = this.getToolkit().getImage("images/green.jpg");
	    Image[] imgFlw = new Image[3];
		imgFlw[0] = this.getToolkit().getImage("images/flower0.png");
		imgFlw[1] = this.getToolkit().getImage("images/flower1.png");
		imgFlw[2] = this.getToolkit().getImage("images/flower2.png");
	
		MediaTracker mt = new MediaTracker(this); //ʵ��ý�������
        mt.addImage(imgBee, 0); //����ͼ�񵽼�������
		mt.addImage(imgBee2, 1);
		mt.addImage(imgBG,2); //����ͼ�񵽼�������
		mt.addImage(imgFlw[0],4);
		mt.addImage(imgFlw[1],5);
		mt.addImage(imgFlw[2],3);
        try {
            mt.waitForAll(); //�ȴ�ͼƬ����
        } catch (Exception ex) {
            ex.printStackTrace();  //���������Ϣ
        } 
		
		Container container=getContentPane(); //�õ���������
		container.setLayout(null);//������BorderLayout������JPanel�ᱻ�ı��С
	    BackGroundJP pGround = new BackGroundJP(imgBG);	
		pGround.setBounds(0,0,BG_WIDTH,BG_HEIGHT);//ֱ�Ӿ�Բ���
		container.add(pGround);
		pGround.setLayout(null);
		
		bees[1] = new HoneyBee(0,200,200,76,true,imgBee);
		bees[2] = new HoneyBee(1,100,100,145,true,imgBee);
		bees[3] = new HoneyBee(2,20,300,240,true,imgBee);
		bees[9] = new Hornet(9,690,500,240,true,imgBee2);
		pGround.add(bees[9]);
		pGround.add(bees[1]);
		pGround.add(bees[2]);
		pGround.add(bees[3]);
		
		//��������ļ������ڴ棬��������flw[]��
        try{
			int i = 0;
			Scanner input = new Scanner(new File("flower.txt"));
			while(input.hasNext()){
				flws[i] = new Flower(Integer.parseInt(input.next()),30+Integer.parseInt(input.next()),
									30+Integer.parseInt(input.next()),10+Integer.parseInt(input.next()),
									imgFlw[Integer.parseInt(input.next())],pGround);
				i++;
			}
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}catch(IOException e) {
			System.err.println(e);
		}
		
        setSize(BG_WIDTH+5,BG_HEIGHT+35); //���ô��ڳߴ�
        setVisible(true); //���ô��ڿ���
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //�رմ���ʱ�˳�����    
		//��������¼�������
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(flag == true)
					flag = false;
				else
					flag = true;
			}
		});
		
		new Thread(new GodThread()).start();
		
    }
	

	
	/**�߳��ڲ��࣬���ڶ�ʱ����ϵͳ���ȷ���*/
	private class GodThread implements Runnable {
		public void run() {
			while(true) {	
				if(flag&&begin){ //begin�������Ƴ���Ľ����flag��������ͣ��Ϸ��
					next();
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void next(){
		//�жϻ������ʱ���Ƿ񵽴��ɻ�
		for(int i = 0;i<20;i++){
			if(flws[i].getTime() == time){
				flowers.add(flws[i]);
				flws[i].getPGround().add(flws[i]);
				repaint();
			}
		} 
		time++;
	//	music.play();
		//3000�ֺ�������
		if(time==4000||count==0||countflower==0){
			game="GAME OVER";
		//	music.stop();
			begin=false;
			Beegoal = count*50;
			try{
				File dirFile = new File("Result");
				if(dirFile.exists() != true) {
					dirFile.mkdir();
				}
				PrintWriter out = new PrintWriter(
								  new FileWriter("Result\\result"+System.currentTimeMillis()+".txt"));
			    
		            out.println("totalHoney: "+BeeFarming.getHoney()+" kg;"); 
					out.println("still alive Bees: "+BeeFarming.count+" bees;");
					int atime = 200-time/20;
					out.println("The time left: "+atime+" S;");
					int goals = BeeFarming.getHoney()+BeeFarming.Beegoal;
					if(count>0&&countflower==0)goals+=atime;
		            if(count==0&&countflower>0)goals-=atime;
					out.println("Final Goals: "+goals+".");
					out.close();

					}catch(FileNotFoundException e){
						System.out.println("Error:Cannot open file for writing.");
					}catch(IOException e){
						System.out.println("Error:Cannot write to file.");
					}
		}
		//�ȶ�һ��ʱ������һ�Σ��۲��ܱ�������趨����·��
		if(angle_damp==0){
			for(int i=1;i<10;i++)
				if(bees[i]!=null)//�۷��Ƿ���ڣ����˻��������ɾ��
					if(bees[i].getlive()==true){
						bees[i].search();					
					}
		}
		//�Ӽȶ�����·�߷���ֱ����һ������
		for(int i=1;i<10;i++)
			if(bees[i]!=null)//�۷��Ƿ���ڣ����˻��������ɾ��
				if(bees[i].getlive()==true){
					bees[i].flying(angle_damp);
				}
		angle_damp ++;	
		if(angle_damp==9){
			angle_damp =0;
		}
	}
	
	/**�۷�ͨ����෽�����Լ���״̬������������
	* @param fs ��¼�۷�״̬�Ķ���
	*/
	public static void update(FlyingStatus fs)
	{
		status[fs.id]=fs;
		//System.out.println("X"+fs.id+"="+fs.x+", Y"+fs.id+"="+fs.y+",a="+fs.angle+",isAlive="+fs.isAlive);
	}
	
	/**�۷�ͨ����෽�����������ѯ�Ӿࡢ���޷�Χ�ڵ�����
	* @param fs ��¼�۷�״̬�Ķ���
	* @return �Զ��Ÿ������ַ������ߴ�š�W��E��N��S��,�����۷��ʽid-angle
	*/
	public static String search(int id)
	{
		
		FlyingStatus fs = status[id];
		String result="";
		
		//�����ж��۷���4���߽�Ĺ�ϵ��(x,y)���۷����ģ�RANGE���Ӿ�,
		int visionX = fs.x + (int)(Math.cos(Math.toRadians(fs.angle))*RANGE[fs.id])-18;
		int visionY = fs.y + (int)(Math.sin(Math.toRadians(fs.angle))*RANGE[fs.id])-18;
		//System.out.println("vx="+fs.x+",vy="+fs.y);
		if(visionX<0)
		{
			result += "*W~";//west
		}	
		else if(visionX>BG_WIDTH)
		{
			result += "*E~";//east
		}
		if(visionY<0)
		{
			result += "*N~";//north
		}
		else if(visionY>BG_HEIGHT)
		{
			result += "*S~";//south
		}
		//��Ҫ�ж��ӽǷ�Χ���Ƿ��л�
		Iterator<Flower> it = flowers.iterator();
		while(it.hasNext()){
			Flower f = it.next();
			if(f.getVolumn()>0){//���Ƿ�����ʾ��
				int fx = (int)(f.getPosition().getX());
				int fy = (int)(f.getPosition().getY());
				int distance1 = (int)(Math.pow((fs.x+18*Math.cos(Math.toRadians(fs.angle))-fx),2)
				            +Math.pow((fs.y+18*Math.sin(Math.toRadians(fs.angle))-fy),2));
				int distance2 = (int)(Math.pow((fs.x-18*Math.cos(Math.toRadians(fs.angle))-fx),2)
				            +Math.pow((fs.y-18*Math.sin(Math.toRadians(fs.angle))-fy),2));
				int distance = (int)(Math.pow(fs.x-fx,2)+Math.pow(fs.y-fy,2));
				if(distance<=4)
					result = result+"-("+f.getVolumn()+",ON)~";//�۷��ڻ���
				else if(distance1<=RANGE[fs.id]*RANGE[fs.id])//ͷ���뻨�ľ���С�ڷ�Χֵ
				{
					//System.out.println("��"+f.getVolumn()+" �ڿ��Ӿ�����:"+distance+" F("+fx+","+fy+")-B("+fs.x+","+fs.y+")");									
					//���з��򳯻�����
					if(distance1<distance2){
						//System.out.println("��"+f.getVolumn()+" �ڿ��Ӿ�����, ͷ����="+distance1+",β����="+distance2);
						double a = getVectorDegree(fs.x,fs.y,fx,fy);
						result = result+"-("+f.getVolumn()+","+a+")~";//�����ڵķ���
					}					
				}
			}
		}

		//���ж��ӽǷ�Χ���Ƿ��������۷�		
		for(int i=0;i<10;i++)
		if(fs.getisAlive()==true){
			if(i!=fs.id&&status[i]!=null&&status[i].isAlive==true)
			{
				FlyingStatus fs1 = status[i];
				int distance = (int)(Math.pow(fs.x-fs1.x,2)+Math.pow(fs.y-fs1.y,2));
				//���۷��Ӿ෶Χ��
				if(distance <= Math.pow(RANGE[fs.id],2))
				{					
					int distance1 = (int)(Math.pow((fs.x+18*Math.cos(Math.toRadians(fs.angle))-fs1.x),2)
				            +Math.pow((fs.y+18*Math.sin(Math.toRadians(fs.angle))-fs1.y),2));
					int distance2 = (int)(Math.pow((fs.x-18*Math.cos(Math.toRadians(fs.angle))-fs1.x),2)
				            +Math.pow((fs.y-18*Math.sin(Math.toRadians(fs.angle))-fs1.y),2));
					 if(distance1<distance2){
						//System.out.println("��"+fs.id+" �����˷�"+fs1.id);
						double a = getVectorDegree(fs.x,fs.y,fs1.x,fs1.y);
						result = result+"+("+fs1.id+","+a+","+fs1.angle+")~";
						//�����ķ�ķ����Լ���ķ��з���		
					}					
				}
			}
		}			
		return result;
	} 
	
	/**����۷�id������2,2���Ƿ��л��ۿ��Բ�
	* @param id �۷�id 
	* @return ����״̬��1-�˴β��껹�л��ۣ�0-����û�л����ˣ�-1-�����޻���
	*/
	public static int pickFlowerHoney(int id){
		//����۷�id����ѯ����Ӧ�Ļ�����ȥ��Ӧ���Ļ���
		int bx = status[id].x;
		int by = status[id].y;
		Iterator<Flower> it = flowers.iterator();
		while(it.hasNext()){
			Flower f = it.next();
			if(f.getVolumn()>0){//���Ƿ�����ʾ��
				int fx = (int)(f.getPosition().getX());
				int fy = (int)(f.getPosition().getY());
				int distance = (int)(Math.pow(bx-fx,2)+Math.pow(by-fy,2));
				if(distance <= 4){
					boolean more = f.consume(1);//���ۼ���
					totalHoney++;//�ܲɼ�������
					if(more)
						return 1;//1��?���ۿɲ�
					else
					    countflower--;
						return 0;//��������һ���ˣ������Ѿ�����
				}
			}
		}
		return -1;//����û�л�
	}
	public static int getHoney()
	{
	   return  totalHoney;
	}	 
	/**������(A,B)��꣬����ʸ��AB�ĽǶȣ���Χ��0-360��X ������ϵĵ��� Y �������ת
	* @param x1,y1,x2,y2 �������A(x1,y1),B(x2,y2) 
	* @return [0,360), ����360��˵�������������
	*/
	public static double getVectorDegree(int x1,int y1,int x2,int y2){
		int deltaY = y2 - y1;
		int deltaX = x2 - x1;
		if(deltaX ==0){//tanΪ���������
			if(deltaY>0)
				return 90;
			if(deltaY<0)
				return 270;
		}else
		{
			double k = (double)deltaY / deltaX;
			if(deltaX>0&&deltaY>=0)
				return Math.toDegrees(Math.atan(k));
			if(deltaX>0&&deltaY<0)
				return 360+Math.toDegrees(Math.atan(k));
			if(deltaX<0)
				return 180+Math.toDegrees(Math.atan(k));
		}
		return 360;//����360��˵�������������
	}
	
	public static void killBee(int id){
	    FlyingStatus fs = status[id];
		int visionX = fs.x + (int)(Math.cos(Math.toRadians(fs.angle))*RANGE[fs.id])-18;
		int visionY = fs.y + (int)(Math.sin(Math.toRadians(fs.angle))*RANGE[fs.id])-18;	
		//�ж��ӽǷ�Χ���Ƿ��������۷�
		for(int i=0;i<9;i++)
			if(status[i]!=null&&status[i].isAlive==true)
			{
				FlyingStatus fs1 = status[i];
				int distance = (int)(Math.pow(fs.x-fs1.x,2)+Math.pow(fs.y-fs1.y,2));
				//�ڻƷ�����Ȧ��Χ��
				if(distance<=16){
					fs1.isAlive=false;
					Hornet hornet = (Hornet) bees[9];
					hornet.isCatched();
					//System.out.println("��"+fs1.id+"�Ѿ����� ��");
					count--;	
				}					
			}
	}
	public static void main(String[] args){
        new BeeFarming();
    }
}
class BackGroundJP extends JPanel{
	private Image bgImg;
	int i;
	
	public BackGroundJP(Image img) {
        this.bgImg = img;
        Dimension size = new Dimension(img.getWidth(null),img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }
	
    public void paintComponent(Graphics g) {
		if(bgImg!=null)
			g.drawImage(bgImg,0,0,this);
			Color c = g.getColor();
			Font f = g.getFont();
			g.setFont(new Font("Arial",Font.PLAIN,30));
			
			int atime = 200-BeeFarming.time/20;
			if(atime>10){
				g.setColor(new Color(128,255,255));
				g.drawString("TIME: "+atime+" S", 500, 30);
			}
			else
			{
			    g.setColor(Color.RED);
				g.drawString("TIME: "+atime+" S", 500, 30);
			}
			g.setColor(new Color(128,255,255));
			g.drawString("totalHoney: "+BeeFarming.getHoney()+" kg", 500, 65); 
			g.drawString("still alive Bees: "+BeeFarming.count, 500, 100);
			int sum= BeeFarming.getHoney()+BeeFarming.Beegoal;
			if(BeeFarming.count>0&&BeeFarming.countflower==0)sum+=atime;
		    if(BeeFarming.count==0&&BeeFarming.countflower>0)sum-=atime;
			g.drawString("Goals: "+sum,500, 135);
			//1kg���ۼ�һ�֣�1ֻ�۷��50�֣�������ǰ����ÿ��ʱ���1�֣����۷���ǰ���⣬ÿ��ʱ���-1��
			g.setFont(new Font("Arial",Font.PLAIN,60));
			g.setColor(Color.RED);
			g.drawString(BeeFarming.game,230, 310);
			repaint();
			g.setColor(c);
			g.setFont(f); 
    }
}


