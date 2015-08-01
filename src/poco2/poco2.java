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
	int [] arrmius = new int [] {-1, -7, 1, 8, 7, 6}; //주변블록 위치 찾기용 북서쪽부터 시계방향
	int [] arrplus = new int [] {-8, -7, -6, 1, 7, -1}; //주변블록 위치 찾기용 북서쪽부터 시계방향
	
	int [][][] wayarr = new int [5][42][42];
	int [] cnt = new int [42];
	int [][] status = new int [5][42];
	int [] useblock = new int [42];
	
	int ev = 5; //오차값 수정용도
	
	//0 스페셜 블럭
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
			int x = 20, y = 425, w = 50, h = 50;		//시작점
			int mx = 0, my = 0, mxsw;
			int tiktok = 0;
			mxsw = i % 7;								//가로열 스위치 담당
			mx = 50 * mxsw;								//가로열 위치잡기 완료
			
			tiktok = i / 7;
			y = (tiktok * 50) + y;
			
			if((i + tiktok) % 2 == 0){
				my = 25;
			}
			
			lbl[i] = new Label();
			lbl[i].setBounds(x + mx, y + my, w, h);		
			lbl[i].setBackground(co[i]);				//레이블 기본컬러
			String lblnum = Integer.toString(i); 		//레이블을 위한 형변환
			lbl[i].setText(lblnum);						//레이블 숫자 지정
	        add(lbl[i]);

		}
        //addKeyListener(new KeyAction());
		//addKeyListener(this);
		setLayout(null);
		setLocation(800, 200);
		setSize(400, 800);
		setTitle("Poco");
		getContentPane().setBackground(Color.darkGray);	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				//끄기버튼구현
		setVisible(true);
		run();

		}
		
	}
	
	
	public void run(){
		try { 
		
		
		int sw = 1;
		System.out.println("run 진입");
		while(true){
			Thread.sleep(10); 
			while(calc){
				
				BufferedImage bfimg = robot.createScreenCapture(new Rectangle(1375, 411, 518, 543));
				
				if(sw == 1){
					for (int i = 0; i < 42; i++) {
						int x = 34, y = (77 - (35 + 8));					//시작점
						int mx = 0, my = 0, mxsw;
						int tiktok = 0;
						mxsw = i % 7;										//가로열 스위치 담당
						mx = 75 * mxsw;										//가로열 위치잡기 완료
								
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
				
				int [] maxcnt = new int	[5]	;												//긴 블록 갯수 저장 카운터 초기화
				int [] maxblock = new int [5];
				
				for(int grid = 0; grid < 5; grid++){
					
					switch(grid) { 
					case 0: // middle
						for (int i = 0; i < 42; i++) {							//색상 갱신 For문 + 블럭에 색상 대역값으로 라벨을 달아준다.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375, posy[i] - 411));
				    		//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//라벨에 색상갱신
				    		
				    		//status 에다가 현재 블럭이 무슨 색상인지 담는다.
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
						for (int i = 0; i < 42; i++) {							//색상 갱신 For문 + 블럭에 색상 대역값으로 라벨을 달아준다.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375 + 28, posy[i] - 411));
							//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//라벨에 색상갱신
				    		
				    		//status 에다가 현재 블럭이 무슨 색상인지 담는다.
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
						for (int i = 0; i < 42; i++) {							//색상 갱신 For문 + 블럭에 색상 대역값으로 라벨을 달아준다.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375 - 28, posy[i] - 411));
							//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//라벨에 색상갱신
				    		
				    		//status 에다가 현재 블럭이 무슨 색상인지 담는다.
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
						for (int i = 0; i < 42; i++) {							//색상 갱신 For문 + 블럭에 색상 대역값으로 라벨을 달아준다.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375, posy[i] - 411 - 28));
							//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//라벨에 색상갱신
				    		
				    		//status 에다가 현재 블럭이 무슨 색상인지 담는다.
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
						for (int i = 0; i < 42; i++) {							//색상 갱신 For문 + 블럭에 색상 대역값으로 라벨을 달아준다.
							co[i] = new Color(bfimg.getRGB(posx[i] - 1375, posy[i] - 411 + 28));
							//System.out.println(co[i] + " " + i);
				    		lbl[i].setBackground(co[i]);						//라벨에 색상갱신
				    		
				    		//status 에다가 현재 블럭이 무슨 색상인지 담는다.
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
					//여기에 알고리즘 구현을 하는데.... 벽짚고가기 알고리즘으로
					
					maxcnt[grid] = 0;
					maxblock[grid] = 0;
													
					//제일 긴 블록 시작점 저장
					for(int u = 0; u < 42; u++){											//카운터 초기화
						cnt[u] = 0;
					}
					
					
					//for문 알고리즘 메일 루틴
					for(int i = 0; i < 42; i++){											// 42개의 블럭에서 제일 긴 블럭을 찾는다.
						int p = i;
						int blocksw = 1;
						
						if(status[grid][i] == 0){
							continue;
						}
						
						for(int u = 0; u < 42; u++){											//블록 사용 여부 초기화
							useblock[u] = 0;
						}
						
						while(blocksw == 1){
							wayarr[grid][i][cnt[i]] = p;												// 현재 블럭 저장
							blocksw = 0;														// 건너갈 블록이 없으면 정지
							
							bing:{
									
								if((p % 7) % 2 == 0){												// 홀수열이다.
									for(int j = 0; j < 6; j++){										// 주변블럭을 탐색한다.
										if(j == 0 || j == 5){										// 오른쪽 줄인가?
											if(p % 7 == 0){
												continue;
											}
										}else if(j == 2 || j == 3){									// 왼쪽 줄인가?
											if(p % 7 == 6){
												continue;
											}
										}
										if(arrmius[j] + p < 0 || arrmius[j] + p > 41){		// 맨 윗줄이나 아랫줄은 아닌가?
											continue;
										}
										if(useblock[arrmius[j] + p] == 1){							// 블럭을 사용했다면 그냥 스킵
											continue;
										}
										
										// 예외처리 끝
										//System.out.println(" i : " + i + " p : " + p + " j : " + j);
		
										if(status[grid][p] == status[grid][arrmius[j] + p]){					// 인접블럭 색깔이 같으면
											
											cnt[i]++;										// 카운트 증가
											useblock[p] = 1;										// 블럭 사용중표기
											blocksw = 1;											// 건너갈 블록을 찾았다 계속하라
											p = arrmius[j] + p;										// 건너갈 블록 주소
											break bing;
											
										}	
									}
									
								}else{									// 짝수열이다.
									for(int j = 0; j < 6; j++){										// 주변블럭을 탐색한다.
			
										if(arrplus[j] + p < 0 || arrplus[j] + p > 41){				// 맨 윗줄이나 아랫줄은 아닌가?
											continue;
										}
										if(useblock[arrplus[j] + p] == 1){							// 블럭을 사용했다면 그냥 스킵
											continue;
										}
			
										// 예외처리 끝
										//System.out.println(" i : " + i + " p : " + p + " j : " + j);
		
										if(status[grid][p] == status[grid][arrplus[j] + p]){					// 인접블럭 색깔이 같으면
											
											cnt[i]++;										// 카운트 증가
											useblock[p] = 1;										// 블럭 사용중표기
											blocksw = 1;											// 건너갈 블록을 찾았다 계속하라
											p = arrplus[j] + p;										// 건너갈 블록 주소
											break bing;
										}	
									}
								}	
							}
						}
						
						if(maxcnt[grid] < cnt[i]){			// 제일 긴것을 찾아라~
							maxcnt[grid] = cnt[i];
							maxblock[grid] = i;				// 제일 긴 블록 시작점 저장
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
				///////////////////////////////////////////////////////////////////////////////////여기에서부터 드로잉!!!
				
				
				
				if (maxcntp > 1 && maxcntp < 20){					// 0부터 시작하고 maxcnt가 2이상 이여야만 3개블록 추출
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
				//제일 길이 긴거 찾아서 드로윙
	
				
				///////////////////////////////
				//5개의 포인트에서 색상 추출 후 가장 긴거 그리기
				//블럭을 아래에서부터 검출 : 동물 먼저 보내야함
				//불 폭탄 색상 추출 필요 50초 이후부터 터뜨릴것 (리스너 제작으로 버튼 제어 필요)
				//물 폭탄의 경우 생상을 두 장소에서 추출후 검출
				//터뜨릴게 없을경우 폭탄먼저
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