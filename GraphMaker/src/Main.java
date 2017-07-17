import static java.lang.Math.round;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Main {
	static JPanel mainPannel;
	static Color netColor = new Color(0xED, 0xED, 0xED);
	static int midX, midY;
	static int tempX, tempY;
	static int size = 20;
	static double step = 0.05;
	static Map<String, Color> functions = new LinkedHashMap<>();
	
//	static void drawFunc(Graphics g, Color color, double from, double to,
//			int midX, int midY, Function<Double, Double> func) {
//		g.setColor(color);
//		double x0 = from;
//		double y0 = func.apply(from);
//
//		for (double x = from; x <= to; x += 0.01) {
//			x = round((x * 100));
//			x /= 100;
//
//			g.drawLine((int) (midX + x0 * size), (int) (midY - y0 * size),
//					(int) (midX + x * size),
//					(int) (midY - func.apply(x) * size));
//			x0 = x;
//			y0 = func.apply(x);
//		}
//	}

	static void drawFunc(Graphics g, Color color, double from, double to, String func) {
		Expression F = new ExpressionBuilder(func)
		    .variables("e", "pi", "x")
		    .build()
		    .setVariable("e", Math.E)
		    .setVariable("pi", Math.PI);

		g.setColor(color);
		double x0 = from;
		F.setVariable("x", from);
		double y0 = F.evaluate();

		for (double x = from; x <= to; x += step) {
			x = round(x * Math.pow(step, -1));
			x /= Math.pow(step, -1);
				
			try {
				if (Math.sqrt( (x-x0)*(x-x0) + (F.setVariable("x", x).evaluate()-y0)*(F.setVariable("x", x).evaluate()-y0) ) <= step*1000 ) {
					g.drawLine((int) (midX + x0 * size), (int) (midY - y0 * size),
							   (int) (midX + x * size), (int) (midY - F.setVariable("x", x).evaluate() * size));
				}
			} catch (Exception e) {} finally {
				x0 = x;
				try {
					y0 = F.setVariable("x", x).evaluate();
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}	
	
	@SuppressWarnings("serial")
	public static void main(String[] arg) {
//		WebLookAndFeel.install();
		new JFrame("Графики") {
			{
		
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setBounds(100, 100, 600, 600);
				
				setJMenuBar(new JMenuBar() {
					{
						add(new JMenuItem("Функции (нажать)") {
							{
								addActionListener(x -> Func.show());
							}
						});
					}
				});
				
				midX = ((int) getWidth() / 2);
				midY = ((int) getHeight() / 2);
				
				setVisible(true);
				
				add(mainPannel = new JPanel() {
					

					{
						addMouseWheelListener(new MouseWheelListener() {
							public void mouseWheelMoved(MouseWheelEvent e) {
								if (e.getWheelRotation() == -1) {
									if (size <300) {
										if (size < 30) {
											size++;
											step = 0.1;
										} else {
											step = 0.01;
											size *= 1.2;
										}
									}
								} else {
									if (size > 5) {
										if (size < 30) {
											size--;
											step = 0.1;
										} else {
											size /= 1.2;
											step = 0.01;
										}
									}
								}
								repaint();
							}

						});
						addMouseMotionListener(new MouseMotionListener() {
							public void mouseMoved(MouseEvent e) {
							}

							public void mouseDragged(MouseEvent e) {
								midX = e.getX();
								midY = e.getY();
								repaint();
							}
						});
					}

					public void update(Graphics g) {
						midX = ((int) getWidth() / 2) + tempX;
						midY = ((int) getHeight() / 2) + tempY;
					}

					public void paint(Graphics g) {
						int i, nums;
						double from, to;
						
						g.setColor(Color.white);
						g.fillRect(0, 0, getWidth(), getHeight());
						g.setColor(Color.black);
						g.drawLine(0, midY, getWidth(), midY);
						g.drawLine(midX, 0, midX, getHeight());
						
						g.setFont(g.getFont().deriveFont(10F));
						
						
						
						for (i = midX; i <= getWidth(); i += size) {
							g.setColor(netColor);
							g.drawLine(i+size, 0, i+size, getHeight());
							g.setColor(Color.black);
							g.drawLine(i, midY - 5, i, midY + 5);
							g.drawLine(i+(size/2), midY - 2, i+(size/2), midY + 2);
						}
						for (i = midX; i >= 0; i -= size) {
							g.setColor(netColor);
							g.drawLine(i-size, 0, i-size, getHeight());
							g.setColor(Color.black);
							g.drawLine(i, midY - 5, i, midY + 5);
							g.drawLine(i-(size/2), midY - 2, i-(size/2), midY + 2);
						}
						for (i = midY; i <= getHeight(); i += size) {
							g.setColor(netColor);
							g.drawLine(0, i+size, getWidth(), i+size);
							g.setColor(Color.black);
							g.drawLine(midX + 5, i, midX - 5, i);
							g.drawLine(midX + 2, i+(size/2), midX - 2, i+(size/2));
						}
						for (i = midY; i >= 0; i -= size) {
							g.setColor(netColor);
							g.drawLine(0, i-size, getWidth(), i-size);
							g.setColor(Color.black);
							g.drawLine(midX + 5, i, midX - 5, i);
							g.drawLine(midX + 2, i-(size/2), midX - 2, i-(size/2));
						}

						for (i = midX, nums = 5; i <= getWidth(); i += size*5, nums+=5) {
							g.drawString(String.valueOf(nums), i-3+size*5, midY+20);
						}
						to = nums;
						for (i = midX, nums = -5; i >= 0; i -= size*5, nums-=5) {
							g.drawString(String.valueOf(nums), i-7-size*5, midY+20);
						}
						from = nums;
						for (i = midY, nums = -5; i <= getHeight(); i += size*5, nums-=5) {
							g.drawString(String.valueOf(nums), midX-25, i+size*5+3);
						}
						for (i = midY, nums = 5; i >= 0; i -= size*5, nums+=5) {
							g.drawString(String.valueOf(nums), midX-20, i-size*5+3);
						}
						
						for(String key : functions.keySet() )
							drawFunc(g, functions.get(key), from, to, key);


					}
				});

			}

		};

	}
}
