import java.awt.*;
import java.util.*;

public class HoneyBee extends Bee{
	
	private int id; //��¼�۷��id
	//��¼�۷�����ʱ�䣬�����ж��۷��Ƿ��Ѿ����������м�ֻ�۷���,Ĭ��Ϊ��ֻ�۷䣬����ʼ��Ϊ0
	private static int livetime[] = {0,0,0};
	//������¼�۷������״̬����ʼ��Ϊtrue
	private static boolean status[] = {true,true,true};
	//������¼�۷仹����ֻ���ʼ��Ϊ��ֻ�����
	private static int BeeAliveNumber = 3;
	
	/*
	 * �����ڸ���������۷�Ӧ��ð��ȥ�ɵ������
	 */
	private static int AdventureRule[] = {0,3,5};
	
	/*
	 * ������¼�۷�����ڵķ���״̬����-1��0��1��2����ȡֵ;
	 * ���Ϊ-1����Ϊû�лƷ���׷���������ɷ��У�
	 * ���Ϊ0����ʾ���ֻƷ䣬���Ʒ���ʱ��δ�����Լ���
	 * ���Ϊ1����ʾ�Ʒ�����׷������ʼ��Ϊ-1
	 * ���Ϊ2����ʾ����ֻ�۷�����
	 */
	private static int flag[] = {-1,-1,-1};
	/*
	 * ������¼�Ʒ��ϴεķ��нǶȣ���Ҫ�ǵ������۷䷢���Լ����ڻƷ��Σ�շ�Χ�ڣ�
	 * ���Ʒ����򷴷�����У�Ӧ�ð�flag��Ϊ0����ѻƷ�ĽǶȴ��ڴ������У�
	 * ���Ʒ������Լ�����������׷�Լ������Լ�ȴδ���֣�Ӧ�ð�flag��Ϊ1
	 * �����ѻƷ�ķ��нǶȴ浽�����飿����ʼ��Ϊ-1
	 */
	private static double HornetAngle[] = {-1,-1,-1};
	
	//������¼��Ұ�ڵ���Ϣ
	private char boundary = 0;
	private ArrayList<HoneyBee.SearchFlower> FlowerSearch = new ArrayList<HoneyBee.SearchFlower>();
	private ArrayList<HoneyBee.SearchBee> BeeSearch = new ArrayList<HoneyBee.SearchBee>();
	
	private int time = 0;//������ʱ�ı��۷���ת����
	private int SpiralDirection = 1;
	
	public HoneyBee(int id,int x, int y, double angle,boolean isAlive,Image img){
		super(id,x,y,angle,isAlive,img);
		this.id = id;
	}
	
	/**�˷�������Ҫ��д�ĺ��Ĵ��룬�۷���۵���Ҫ�����ڴ�������*/
	public void search(){
		//��ʱ�ı��Լ��ķ��� 
		time++;
		if(time>40){
			time = 0;
			SpiralDirection = SpiralDirection==1?-1:1;
		}
		livetime[id]++;//������ֵ��1
		UpdateStatus();
		//�õ���һ�ε���Ұ��Ϣ
		String strVision = BeeFarming.search(id);
		System.out.println(strVision);
		
		AnalyseSearch(strVision);
		UpdateFlag();//����flag��״̬	
		angle = SpiralStrategy();
		ratoteImage(angle);
		setXYs(0);
	}

	private double DifferenceAngle(double angle1,double angle2){
		
		double a = angle1%360;
		double b = angle2%360;
		if(a>b){
			return a-b>b+360-a?b+360-a:a-b;
		}
		else{
			return b-a<a+360-b?b-a:a+360-b;
		}
	}
	
	/*
     *��������flag״̬���Ե�ֵ
     */
	private void UpdateFlag() {
		
		//���û���۷���Ϣ���򱣳��ϴε�״̬����
		//����ֻ�Է������۷���Ϣ��������д���
		if(BeeSearch.size()>0){
			HoneyBee.SearchBee SB = new HoneyBee.SearchBee(BeeSearch.get(BeeSearch.size()-1));//�õ����һֻ�۷����Ϣ
			if(SB.id==9){//����ֻƷ䣬�������۷�״̬��Ϊ-1
				for(int i = 0;i<flag.length;i++){//���ֻƷ���Ĭ��Ϊ�����۷��ǰ�ȫ�ģ���״̬����Ϊ-1
					if(i!=id)
					    flag[i] = -1;
				}
				//���Ʒ仹û�з����ң���״̬��Ϊ0
				if(DifferenceAngle(SB.DirectionAngle,SB.FlowerAngle)<90)
				{
					flag[id] = 0;
					HornetAngle[id] = SB.FlowerAngle;
				}
				else{//��֮��Ʒ�Ҳ��������
					flag[id] = 1;
				}
				if(BeeSearch.size()>1){//��������������۷�
					for(int j = 0;j<BeeSearch.size()-1;j++){//������Ұ�ڵ������۷���д���
						if(DifferenceAngle(SB.FlowerAngle,BeeSearch.get(j).FlowerAngle)<90)
						{
							flag[BeeSearch.get(j).id] = 1;//��Ϊ��һֻ�۷��Ѿ��������ˣ��򷢳�����
							HornetAngle[BeeSearch.get(j).id] = SB.FlowerAngle;
						}
						else{//�Ʒ����۷�ķ��з����౳��
							flag[BeeSearch.get(j).id] = 0;
							HornetAngle[BeeSearch.get(j).id] = SB.FlowerAngle;
						}
					}//for
				}//if
			}//
			else{//���δ���ֻƷ䣬ֻ�����۷�
				switch(flag[id]){
				case 1://����Լ��ڱ�׷��
					for(int i = 0;i<BeeSearch.size();i++){
						flag[id] = 0;
						HornetAngle[id] = angle;
					}
					break;
				case 2://����Լ��������������򲻸ı������Ϣ
					for(int i=id+1;i<flag.length;i++){
						flag[i] = -1;
						HornetAngle[id] = angle;
					}
					break;
				default:
					for(int i = 0;i<BeeSearch.size();i++){
						if(flag[BeeSearch.get(i).id]==1){//���Է��ڱ��Ʒ�׷
							flag[id] = 0;
							HornetAngle[id] = BeeSearch.get(i).FlowerAngle;
						}
						else{
							flag[id] = 2;
							HornetAngle[id] = BeeSearch.get(i).FlowerAngle;
						}
					}
					break;
				}
			}
		}//if
	}

	/*
	 * ����������Ұ�е���Ϣ
	 */
	private void AnalyseSearch(String strVision) {
		//�����һ�ε���Ϣ
		boundary = 0;
		FlowerSearch.clear();
		BeeSearch.clear();
		if(strVision.length()==0){//���û�еõ��κ���Ϣ���򷵻�
			return;
		}
		String[] strs = strVision.split("~");
		for(int i=0;i<strs.length;i++){
			//�������*Ϊ�׵��ַ���������˱�
			if(strs[i].indexOf('*')==0){			
				boundary = strs[i].charAt(1);
			}		
			//�������-Ϊ�׵��ַ���������˻���������������һ�仨�ɣ���ʹͬʱ�����������
			if(strs[i].indexOf('-')==0)
			{
				String strTmp = strs[i];
				int start = strTmp.indexOf('(');
				int end = strTmp.indexOf(',');
				String s = strTmp.substring(start+1,end);
				int honey = new Integer(s).intValue();
				strTmp = strTmp.substring(end+1);
				end = strTmp.indexOf(')');
				s = strTmp.substring(0,end);
				if(!s.equals("ON")){
					double a = new Double(s).doubleValue();
					HoneyBee.SearchFlower SF = new HoneyBee.SearchFlower(honey,a%360);
					FlowerSearch.add(SF);
				}
				//System.out.println(strs[i]);
			}
			if(strs[i].indexOf('+')==0){
				String strTmp = strs[i];
				int start = strTmp.indexOf('(');
				int end = strTmp.indexOf(',');
				String s = strTmp.substring(start+1,end);
				int id = new Integer(s).intValue();
				strTmp = strTmp.substring(end+1);
				end = strTmp.indexOf(',');
				s = strTmp.substring(0,end);
				double a = new Double(s).doubleValue()%360;
				strTmp = strTmp.substring(end+1);
				end = strTmp.indexOf(')');
				s = strTmp.substring(0,end);
				double b = new Double(s).doubleValue()%360;
				HoneyBee.SearchBee SB = new HoneyBee.SearchBee(id,a,b);
				BeeSearch.add(SB);
				//System.out.println(strs[i]);
			}
		}
	}


	/*
	 * �����жϸ����۷�����״̬�������ֻ�۷�Ĵ��ʱ�����2���ϣ�����Ϊ���ʱ���ٵ���ֻ�۷�����
	 * ����״̬��Ϊfalse�������status����������BeeAliveNumber����
	 */
	private void UpdateStatus() {
		//�ж��۷�Ĵ��״̬
		for(int i = 0;i<livetime.length-1;i++){
			for(int j = i+1;j<livetime.length;j++){
				if(livetime[i]-livetime[j]>1){
					status[j] = false;
				}
				if(livetime[i]-livetime[j]<-1){
					status[i] = false;
				}
			}
		}
		int min = 0;
		//���»������۷��ֻ��
		BeeAliveNumber = 0;
		for(int i = 0;i<status.length;i++){
			if(status[i]==true)
			{
				BeeAliveNumber ++;
				if(min>i)
					min = i;
			}
		}
		
		//����HornetAngle
		if(id==min){
			HornetAngle[0] = -1;
			HornetAngle[1] = -1;
			HornetAngle[2] = -1;
		}
	}

	/*
                 ������ԣ�����������ƣ���Direction������
    */
	private double SpiralStrategy() {
		
		double nextAngle = 0;
		switch(flag[id])
		{
		case -1://�Լ��������ɷ����ڣ�Ӧ��ѡ��һ��������Ļ���
			if(FlowerSearch.size()>0){//������л�������������Ļ���
				int max = 0;
				for(int i = 1;i<FlowerSearch.size();i++){
					if(FlowerSearch.get(i).volumn>FlowerSearch.get(max).volumn){
						max = i;
					}
				}
				nextAngle = FlowerSearch.get(max).FlowerAngle;
			}
			else if(boundary!=0){//������˱߽�
				nextAngle = BoundaryNextAngle(boundary);
			}
			else{//δ�����κ���Ϣ����Ĭ�Ϸ���
				nextAngle = angle + 5*SpiralDirection;
			}
			break;
		case 0://��ʾ���ֻƷ䣬���Ʒ���ʱ��δ�����Լ�
			
			flag[id] = -1;
			//�õ��Ʒ����Ϣ
			double FlowerAngle = HornetAngle[id];
			if(FlowerSearch.size()>0){//������л�
				int temp = 0;
				int max = -1;
				for(int i = 0;i<FlowerSearch.size();i++){
					HoneyBee.SearchFlower SF = new HoneyBee.SearchFlower(FlowerSearch.get(i));
					if(DifferenceAngle(SF.FlowerAngle,FlowerAngle)<90)
					{
						if(max==-1){
							max = i;
						}
						else{
							if(FlowerSearch.get(i).volumn>FlowerSearch.get(max).volumn)
								max = i;
						}
					}
					if(FlowerSearch.get(i).volumn>FlowerSearch.get(temp).volumn){
						temp = i;
					}
				}
				if(max == -1){//���û����ȫ��������Ļ����������ʺϵ�
					nextAngle = FlowerSearch.get(temp).FlowerAngle;
					while(true){
						if(DifferenceAngle(nextAngle,FlowerAngle)>10)
						{
							break;
						}
						nextAngle++;
					}
					return nextAngle;
				}
				nextAngle = FlowerSearch.get(max).FlowerAngle;
			}
			else{//���δ���ֻ�
				if(boundary!=0){//���ֱ߽�
					nextAngle = BoundaryNextAngle(boundary);
				}
				else{
				    Random ra = new Random();
				    nextAngle = FlowerAngle + 135 + ra.nextInt(90);
				}
			}
			break;
		case 1://��ʾ�Ʒ�����׷��		
			if(FlowerSearch.size()>0){//������л�
				int temp = 0;
				int max = -1;
				for(int i = 0;i<FlowerSearch.size();i++){
					HoneyBee.SearchFlower SF = new HoneyBee.SearchFlower(FlowerSearch.get(i));
					if(DifferenceAngle(SF.FlowerAngle,angle)<30)
					{
						if(SF.volumn<=AdventureRule[BeeAliveNumber-1])
						{
							if(max==-1){
								max = i;
							}
							else{//ȡ��Сֵ
								if(FlowerSearch.get(i).volumn<FlowerSearch.get(max).volumn)
									max = i;
							}
						}
					}
					if(FlowerSearch.get(i).volumn>FlowerSearch.get(temp).volumn){
						temp = i;
					}
				}
				if(max == -1){//���û����ȫ��������Ļ����������ʺϵ�
					nextAngle = FlowerSearch.get(temp).FlowerAngle + 10;
					return nextAngle;
				}
				nextAngle = FlowerSearch.get(max).FlowerAngle;
			}
			else if(boundary!=0){
				nextAngle = BoundaryNextAngle(boundary);
			}
			else{//����30�Ƚ���ת��
				Random ra = new Random();
				nextAngle = angle - 30 +ra.nextInt(60);
			}
			break;
		case 2://��ʾ����ֻ�۷�����
			flag[id] = -1;
			double BeeAngle = HornetAngle[id];
			if(FlowerSearch.size()>0){//������л���ѡ�������Ļ�
				int max = 0;
				for(int i = 1;i<FlowerSearch.size();i++){
					if(FlowerSearch.get(i).volumn>FlowerSearch.get(max).volumn){
							max = i;
					}
				}
				nextAngle = FlowerSearch.get(max).FlowerAngle;
			}
			else if(boundary!=0){
				nextAngle = BoundaryNextAngle(boundary);
			}
			else{
				Random ra = new Random();
				nextAngle = BeeAngle + 120 +ra.nextInt(120);
			}
			break;
		default://���������쳣ģʽֵ
			nextAngle = angle;
			break;
		}
		return nextAngle;
		
	}

    private double BoundaryNextAngle(char strs){
		
		double nextAngle = angle%360;
		Random ra = new Random();
		if(strs=='E'){ //����������Ƕ��߽�
			if(angle > 180){//����۷����ڷ��еĽǶȴ���180�����������Ϸ���
				nextAngle = 270 - ra.nextInt(45);
				SpiralDirection = -1;
			}
			else{//����۷����ڷ��еĽǶ�С��180�����������·���
				nextAngle = 90 + ra.nextInt(45);
				SpiralDirection = 1;
			}
		}
		else if(strs=='W'){
			if(angle > 180){//����۷����ڷ��еĽǶȴ���180�����������Ϸ���
				nextAngle = 270 + ra.nextInt(45);
				SpiralDirection = 1;
			}
			else{//����۷����ڷ��еĽǶ�С��180�����������·���
				nextAngle = 90 - ra.nextInt(45);
				SpiralDirection = -1;
			}
		}
		else if(strs=='N'){
			if(angle > 270){//����۷����ڷ��еĽǶȴ���270�����������Ϸ���
				nextAngle = ra.nextInt(45);	
				SpiralDirection = 1;
			}
			else{//����۷����ڷ��еĽǶ�С��270�����������Ϸ���
				nextAngle = 180 - ra.nextInt(45);
				SpiralDirection = -1;
			}
		}
		else{
			if(angle > 90){//����۷����ڷ��еĽǶȴ���180�����������·���
				nextAngle = 180 + ra.nextInt(45);
				SpiralDirection = 1;
			}
			else{//����۷����ڷ��еĽǶ�С��180�����������·���
				nextAngle = 360 - ra.nextInt(45);
				SpiralDirection = -1;
			}
		}
		return nextAngle;
	}
	
    class SearchFlower{
    	private int volumn;
    	private double FlowerAngle;
    	public SearchFlower(int volumn,double FlowerAngle){
    		this.volumn = volumn;
    		this.FlowerAngle = FlowerAngle;
    	}
    	public SearchFlower(HoneyBee.SearchFlower SF){
    		this.volumn = SF.volumn;
    		this.FlowerAngle = SF.FlowerAngle;
    	}
    }
    
    class SearchBee{
    	int id;
    	double DirectionAngle;
    	double FlowerAngle;
    	public SearchBee(int id,double DirectionAngle,double FlowerAngle){
    		this.id = id;
    		this.DirectionAngle = DirectionAngle;
    		this.FlowerAngle = FlowerAngle;
    	}
    	public SearchBee(HoneyBee.SearchBee SB){
    		this.id = SB.id;
    		this.DirectionAngle = SB.DirectionAngle;
    		this.FlowerAngle = SB.FlowerAngle;
    	}
    }
    
}

