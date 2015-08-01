package poco2;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class poco2 extends JFrame implements NativeKeyListener {
	public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VK_ENTER) {
                calc = true;
                System.out.println(calc);
        }
	}
	
	public void nativeKeyReleased(NativeKeyEvent e) {
		//System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}
	
	public void nativeKeyTyped(NativeKeyEvent e) {
		//System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
	}
	
	boolean calc = false;	
	int hitcnt = 0;
	
	int [] blockren = new int [42];
	int [] arrmius = new int [] {-1, -7, 1, 8, 7, 6}; //�ֺ���� ��ġ ã��� �ϼ��ʺ��� �ð����
	int [] arrplus = new int [] {-8, -7, -6, 1, 7, -1}; //�ֺ���� ��ġ ã��� �ϼ��ʺ��� �ð����
	
	int [][][] wayarr = new int [5][42][42];
	int [] cnt = new int [42];
	int [][] status = new int [5][42];
	int [] useblock = new int [42];
	
	int ev = 5; //������ �����뵵
	
	//0 ����� ��
	//1 Blue
	//2 Green
	//3 Purple
	//4 Red
	//5 Yellow
	
	Robot robot = new Robot();
	
	Color[] co = new Color[42];
	Label[] lbl = new Label[42];
	
	int [] posx = new int [42];
	int [] posy = new int [42];
	
	poco2(int T) throws AWTException{
		
		if(T == 0){
		
		
		for(int i = 0; i < 42; i++){
			int x = 20, y = 425, w = 50, h = 50;		//������
			int mx = 0, my = 0, mxsw;
			int tiktok = 0;
			mxsw = i % 7;								//���ο� ����ġ ���
			mx = 50 * mxsw;								//���ο� ��ġ��� �Ϸ�
			
			tiktok = i / 7;
			y = (tiktok * 50) + y;
			
			if((i + tiktok) % 2 == 0){
				my = 25;
			}
			
			lbl[i] = new Label();
			lbl[i].setBounds(x + mx, y + my, w, h);		
			lbl[i].setBackground(co[i]);				//���̺� �⺻�÷�
			String lblnum = Integer.toString(i); 		//���̺��� ���� ����ȯ
			lbl[i].setText(lblnum);						//���̺� ���� ����
	        add(lbl[i]);

		}
        //addKeyListener(new KeyAction());
		//addKeyListener(this);
		setLayout(null);
		setLocation(800, 200);
		setSize(400, 800);
		setTitle("Poco");
		getContentPane().setBackground(Color.darkGray);	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				//�����ư����
		setVisible(true);
		run();

		}
		
	}
	
	
	public void run(){
		try { 
		
		
		int sw = 1;
		System.out.println("run ����");
		while(true){
			Thread.sleep(10); 
			while(calc){
				
				BufferedImage bfimg = robot.createScreenCapture(new Rectangle(1375, 411, 518, 543));
				
				if(sw == 1){
					for (int i = 0; i < 42; i++) {
						int x = 34, y = (77 - (35 + 8));					//������
						int mx = 0, my = 0, mxsw;
						int tiktok = 0;
						mxsw = i % 7;										//���ο� ����ġ ���
						mx = 75 * mxsw;										//���ο� ��ġ��� �Ϸ�
								
						tiktok = i / 7;
						y = (tiktok * (70 + 16)) + y;
						
						if((i + tiktok) % 2 == 0){
							my = (35 + 8);
						}
						posx[i] = x + mx + 1375;
						posy[i] = y + my + 411;
						sw = 0;
					}
				}
				
				int [] maxcnt = new int	[5]	;												//�� ��� ���� ���� ī���� �ʱ�ȭ
				int [] maxblock = new int [5];
				
				for(int grid = 0; grid < 5; grid++){
					
					switch(grid) { 
					case 0: // middle
						for (int i = 0; i < 42; i++) {							//���� ���� For�� + ���� ���� �뿪������ ���� �޾��ش�.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375, posy[i] - 411));
				    		//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//�󺧿� ���󰻽�
				    		
				    		//status ���ٰ� ���� ���� ���� �������� ��´�.
				    		if ((co[i].getRed() >= 28) && (co[i].getRed() <= 40) && (co[i].getGreen() >= 105) && (co[i].getGreen() <= 119) && (co[i].getBlue() >= 154) && (co[i].getBlue() <= 167)){
				    			status[grid][i] = 1; //Blue
				    		}else if((co[i].getRed() >= 96) && (co[i].getRed() <= 106) && (co[i].getGreen() >= 121) && (co[i].getGreen() <= 131) && (co[i].getBlue() >= 0) && (co[i].getBlue() <= 14)){
				    			status[grid][i] = 2; //Green
				    		}else if((co[i].getRed() >= 148) && (co[i].getRed() <= 162) && (co[i].getGreen() >= 79) && (co[i].getGreen() <= 91) && (co[i].getBlue() >= 155) && (co[i].getBlue() <= 167)){
				    			status[grid][i] = 3; //Purple
				    		}else if((co[i].getRed() >= 184) && (co[i].getRed() <= 197) && (co[i].getGreen() >= 61) && (co[i].getGreen() <= 73) && (co[i].getBlue() >= 69) && (co[i].getBlue() <= 80)){
				    			status[grid][i] = 4; //Red
				    		}else if((co[i].getRed() >= 186) && (co[i].getRed() <= 196) && (co[i].getGreen() >= 125) && (co[i].getGreen() <= 135) && (co[i].getBlue() >= 3) && (co[i].getBlue() <= 19)){
				    			status[grid][i] = 5; //Yellow
				    		}else{
				    			status[grid][i] = 0; //Speacial
				    		}
				    		//System.out.println(status[grid][i] + " " + i);
						}
						break; 
					case 1: // right
						for (int i = 0; i < 42; i++) {							//���� ���� For�� + ���� ���� �뿪������ ���� �޾��ش�.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375 + 28, posy[i] - 411));
							//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//�󺧿� ���󰻽�
				    		
				    		//status ���ٰ� ���� ���� ���� �������� ��´�.
				    		if ((co[i].getRed() >= (32 - ev)) && (co[i].getRed() <= (45 + ev)) && (co[i].getGreen() >= (135)) && (co[i].getGreen() <= (141 + ev)) && (co[i].getBlue() >= (192 - ev)) && (co[i].getBlue() <= (200 + ev))){
				    			status[grid][i] = 1; //Blue
				    		}else if((co[i].getRed() >= (128 - ev)) && (co[i].getRed() <= (136 + ev)) && (co[i].getGreen() >= (159 - ev)) && (co[i].getGreen() <= (167 + ev)) && (co[i].getBlue() >= (4 - ev)) && (co[i].getBlue() <= (21 + ev))){
				    			status[grid][i] = 2; //Green
				    		}else if((co[i].getRed() >= (175 - ev)) && (co[i].getRed() <= (190 + ev)) && (co[i].getGreen() >= (91 - ev)) && (co[i].getGreen() <= (110 + ev)) && (co[i].getBlue() >= (182 - ev)) && (co[i].getBlue() <= (199 + ev))){
				    			status[grid][i] = 3; //Purple
				    		}else if((co[i].getRed() >= (216 - ev)) && (co[i].getRed() <= (228 + ev)) && (co[i].getGreen() >= (75 - ev)) && (co[i].getGreen() <= (91 + ev)) && (co[i].getBlue() >= (83 - ev)) && (co[i].getBlue() <= (99 + ev))){
				    			status[grid][i] = 4; //Red
				    		}else if((co[i].getRed() >= (218 - ev)) && (co[i].getRed() <= (225 + ev)) && (co[i].getGreen() >= (148 - ev)) && (co[i].getGreen() <= (155 + ev)) && (co[i].getBlue() >= (16 - ev)) && (co[i].getBlue() <= (23 + ev))){
				    			status[grid][i] = 5; //Yellow
				    		}else{
				    			status[grid][i] = 0; //Speacial
				    		}
				    		//System.out.println(status[grid][i] + " " + i);
						}
						break;
					case 2: //left
						for (int i = 0; i < 42; i++) {							//���� ���� For�� + ���� ���� �뿪������ ���� �޾��ش�.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375 - 28, posy[i] - 411));
							//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//�󺧿� ���󰻽�
				    		
				    		//status ���ٰ� ���� ���� ���� �������� ��´�.
				    		if ((co[i].getRed() >= (50 - ev)) && (co[i].getRed() <= (58 + ev)) && (co[i].getGreen() >= (152 - ev)) && (co[i].getGreen() <= (156 + ev)) && (co[i].getBlue() >= (212 - ev)) && (co[i].getBlue() <= (223 + ev))){
				    			status[grid][i] = 1; //Blue
				    		}else if((co[i].getRed() >= (151 - ev)) && (co[i].getRed() <= (156 + ev)) && (co[i].getGreen() >= (190 - ev)) && (co[i].getGreen() <= (194 + ev)) && (co[i].getBlue() >= (0 - ev)) && (co[i].getBlue() <= (17 + ev))){
				    			status[grid][i] = 2; //Green
				    		}else if((co[i].getRed() >= (211 - ev)) && (co[i].getRed() <= (216 + ev)) && (co[i].getGreen() >= (140 - ev)) && (co[i].getGreen() <= (148 + ev)) && (co[i].getBlue() >= (216 - ev)) && (co[i].getBlue() <= (222 + ev))){
				    			status[grid][i] = 3; //Purple
				    		}else if((co[i].getRed() >= (233 - ev)) && (co[i].getRed() <= (245 + ev)) && (co[i].getGreen() >= (121 - ev)) && (co[i].getGreen() <= (131 + ev)) && (co[i].getBlue() >= (118 - ev)) && (co[i].getBlue() <= (130 + ev))){
				    			status[grid][i] = 4; //Red
				    		}else if((co[i].getRed() >= (230 - ev)) && (co[i].getRed() <= (235 + ev)) && (co[i].getGreen() >= (193 - ev)) && (co[i].getGreen() <= (197 + ev)) && (co[i].getBlue() >= (9 - ev)) && (co[i].getBlue() <= (21 + ev))){
				    			status[grid][i] = 5; //Yellow
				    		}else{
				    			status[grid][i] = 0; //Speacial
				    		}
				    		//System.out.println(status[grid][i] + " " + i);
						}
						break; 
					case 3: //top
						for (int i = 0; i < 42; i++) {							//���� ���� For�� + ���� ���� �뿪������ ���� �޾��ش�.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375, posy[i] - 411 - 28));
							//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//�󺧿� ���󰻽�
				    		
				    		//status ���ٰ� ���� ���� ���� �������� ��´�.
				    		if ((co[i].getRed() >= (60 - ev)) && (co[i].getRed() <= (74 + ev)) && (co[i].getGreen() >= (150 - ev)) && (co[i].getGreen() <= (162 + ev)) && (co[i].getBlue() >= (204 - ev)) && (co[i].getBlue() <= (219 + ev))){
				    			status[grid][i] = 1; //Blue
				    		}else if((co[i].getRed() >= (155 - ev)) && (co[i].getRed() <= (164 + ev)) && (co[i].getGreen() >= (188 - ev)) && (co[i].getGreen() <= (195 + ev)) && (co[i].getBlue() >= (21 - ev)) && (co[i].getBlue() <= (40 + ev))){
				    			status[grid][i] = 2; //Green
				    		}else if((co[i].getRed() >= (204 - ev)) && (co[i].getRed() <= (210 + ev)) && (co[i].getGreen() >= (145 - ev)) && (co[i].getGreen() <= (159 + ev)) && (co[i].getBlue() >= (209 - ev)) && (co[i].getBlue() <= (217 + ev))){
				    			status[grid][i] = 3; //Purple
				    		}else if((co[i].getRed() >= (228 - ev)) && (co[i].getRed() <= (237 + ev)) && (co[i].getGreen() >= (126 - ev)) && (co[i].getGreen() <= (137 + ev)) && (co[i].getBlue() >= (126 - ev)) && (co[i].getBlue() <= (137 + ev))){
				    			status[grid][i] = 4; //Red
				    		}else if((co[i].getRed() >= (221 - ev)) && (co[i].getRed() <= (229 + ev)) && (co[i].getGreen() >= (197 - ev)) && (co[i].getGreen() <= (200 + ev)) && (co[i].getBlue() >= (23 - ev)) && (co[i].getBlue() <= (47 + ev))){
				    			status[grid][i] = 5; //Yellow
				    		}else{
				    			status[grid][i] = 0; //Speacial
				    		}
				    		//System.out.println(status[grid][i] + " " + i);
						}
						break;  
					case 4: //bottom
						for (int i = 0; i < 42; i++) {							//���� ���� For�� + ���� ���� �뿪������ ���� �޾��ش�.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375, posy[i] - 411 + 28));
							//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//�󺧿� ���󰻽�
				    		
				    		//status ���ٰ� ���� ���� ���� �������� ��´�.
				    		if ((co[i].getRed() >= (44 - ev)) && (co[i].getRed() <= (60 + ev)) && (co[i].getGreen() >= (132 - ev)) && (co[i].getGreen() <= (141 + ev)) && (co[i].getBlue() >= (1177 - ev)) && (co[i].getBlue() <= (200 + ev))){
				    			status[grid][i] = 1; //Blue
				    		}else if((co[i].getRed() >= (127 - ev)) && (co[i].getRed() <= (135 + ev)) && (co[i].getGreen() >= (159 - ev)) && (co[i].getGreen() <= (167 + ev)) && (co[i].getBlue() >= (10 - ev)) && (co[i].getBlue() <= (22 + ev))){
				    			status[grid][i] = 2; //Green
				    		}else if((co[i].getRed() >= (171 - ev)) && (co[i].getRed() <= (185 + ev)) && (co[i].getGreen() >= (96 - ev)) && (co[i].getGreen() <= (118 + ev)) && (co[i].getBlue() >= (178 - ev)) && (co[i].getBlue() <= (192 + ev))){
				    			status[grid][i] = 3; //Purple
				    		}else if((co[i].getRed() >= (210 - ev)) && (co[i].getRed() <= (221 + ev)) && (co[i].getGreen() >= (82 - ev)) && (co[i].getGreen() <= (102 + ev)) && (co[i].getBlue() >= (89 - ev)) && (co[i].getBlue() <= (102 + ev))){
				    			status[grid][i] = 4; //Red
				    		}else if((co[i].getRed() >= (211 - ev)) && (co[i].getRed() <= (220 + ev)) && (co[i].getGreen() >= (152 - ev)) && (co[i].getGreen() <= (164 + ev)) && (co[i].getBlue() >= (9 - ev)) && (co[i].getBlue() <= (29 + ev))){
				    			status[grid][i] = 5; //Yellow
				    		}else{
				    			status[grid][i] = 0; //Speacial
				    		}
				    		//System.out.println(status[i] + " " + i);
						}
						break; 
					}
	
					/////////////////////////////////////////////////////////////////////////
					//���⿡ �˰��� ������ �ϴµ�.... ��¤���� �˰�������
					
					maxcnt[grid] = 0;
					maxblock[grid] = 0;
													
					//���� �� ��� ������ ����
					for(int u = 0; u < 42; u++){											//ī���� �ʱ�ȭ
						cnt[u] = 0;
					}
					
					
					//for�� �˰��� ���� ��ƾ
					for(int i = 0; i < 42; i++){											// 42���� ������ ���� �� ���� ã�´�.
						int p = i;
						int blocksw = 1;
						
						if(status[grid][i] == 0){
							continue;
						}
						
						for(int u = 0; u < 42; u++){											//��� ��� ���� �ʱ�ȭ
							useblock[u] = 0;
						}
						
						while(blocksw == 1){
							wayarr[grid][i][cnt[i]] = p;												// ���� �� ����
							blocksw = 0;														// �ǳʰ� ����� ������ ����
							
							bing:{
									
								if((p % 7) % 2 == 0){												// Ȧ�����̴�.
									for(int j = 0; j < 6; j++){										// �ֺ����� Ž���Ѵ�.
										if(j == 0 || j == 5){										// ������ ���ΰ�?
											if(p % 7 == 0){
												continue;
											}
										}else if(j == 2 || j == 3){									// ���� ���ΰ�?
											if(p % 7 == 6){
												continue;
											}
										}
										if(arrmius[j] + p < 0 || arrmius[j] + p > 41){		// �� �����̳� �Ʒ����� �ƴѰ�?
											continue;
										}
										if(useblock[arrmius[j] + p] == 1){							// ���� ����ߴٸ� �׳� ��ŵ
											continue;
										}
										
										// ����ó�� ��
										//System.out.println(" i : " + i + " p : " + p + " j : " + j);
		
										if(status[grid][p] == status[grid][arrmius[j] + p]){					// ������ ������ ������
											
											cnt[i]++;										// ī��Ʈ ����
											useblock[p] = 1;										// �� �����ǥ��
											blocksw = 1;											// �ǳʰ� ����� ã�Ҵ� ����϶�
											p = arrmius[j] + p;										// �ǳʰ� ��� �ּ�
											break bing;
											
										}	
									}
									
								}else{									// ¦�����̴�.
									for(int j = 0; j < 6; j++){										// �ֺ����� Ž���Ѵ�.
			
										if(arrplus[j] + p < 0 || arrplus[j] + p > 41){				// �� �����̳� �Ʒ����� �ƴѰ�?
											continue;
										}
										if(useblock[arrplus[j] + p] == 1){							// ���� ����ߴٸ� �׳� ��ŵ
											continue;
										}
			
										// ����ó�� ��
										//System.out.println(" i : " + i + " p : " + p + " j : " + j);
		
										if(status[grid][p] == status[grid][arrplus[j] + p]){					// ������ ������ ������
											
											cnt[i]++;										// ī��Ʈ ����
											useblock[p] = 1;										// �� �����ǥ��
											blocksw = 1;											// �ǳʰ� ����� ã�Ҵ� ����϶�
											p = arrplus[j] + p;										// �ǳʰ� ��� �ּ�
											break bing;
										}	
									}
								}	
							}
						}
						
						if(maxcnt[grid] < cnt[i]){			// ���� ����� ã�ƶ�~
							maxcnt[grid] = cnt[i];
							maxblock[grid] = i;				// ���� �� ��� ������ ����
						}
					}
				}	
				///////////////////////////////////////////////////////////////////////////////////
				int maxcntp = 0;
				int maxblockp = 0;
				int gridpos = 0;
				for(int grid = 0; grid < 5; grid++){
					if(maxcntp < maxcnt[grid]){
						maxcntp = maxcnt[grid];
						maxblockp = maxblock[grid];
						gridpos = grid;
					}
				}
				///////////////////////////////////////////////////////////////////////////////////���⿡������ �����!!!
				
				
				
				if (maxcntp > 1 && maxcntp < 20){					// 0���� �����ϰ� maxcnt�� 2�̻� �̿��߸� 3����� ����
					System.out.println("maxcnt : " + maxcntp + "  maxblock : " + maxblockp + "  grid : " + gridpos );
					System.out.println(hitcnt);
					int mousesw = 0;
					
					for(int i = 0; i <= maxcntp; i++){
						robot.mouseMove(posx[wayarr[gridpos][maxblockp][i]], posy[wayarr[gridpos][maxblockp][i]]);
						if(mousesw == 0){
							robot.delay(25);
							robot.mousePress(InputEvent.BUTTON1_MASK);
							hitcnt++;
							mousesw = 1;
						}
						robot.delay(40);
						robot.waitForIdle();
						
					}
					
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					robot.delay(50);
				}
				//���� ���� ��� ã�Ƽ� �����
	
				
				///////////////////////////////
				//5���� ����Ʈ���� ���� ���� �� ���� ��� �׸���
				//���� �Ʒ��������� ���� : ���� ���� ��������
				//�� ��ź ���� ���� �ʿ� 50�� ���ĺ��� �Ͷ߸��� (������ �������� ��ư ���� �ʿ�)
				//�� ��ź�� ��� ������ �� ��ҿ��� ������ ����
				//�Ͷ߸��� ������� ��ź����
	        }
		}
		} catch(Exception e) { 
			e.printStackTrace(); 
		} 
		
	}
	public static void main(String[] args) throws AWTException, InterruptedException{
		poco2 T1 = new poco2(1);
		try {
            GlobalScreen.registerNativeHook();
	    }
	    catch (NativeHookException ex) {
	            System.err.println("There was a problem registering the native hook.");
	            System.err.println(ex.getMessage());
	
	            System.exit(1);
	    }
	
	    //Construct the example object and initialze native hook.
	    GlobalScreen.getInstance().addNativeKeyListener(new poco2(0));
			//new poco01();
	}
}