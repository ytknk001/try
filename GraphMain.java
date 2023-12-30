import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class GraphMain {
	
	private static final int MaxInt = 22222;
	private static Scanner input = new Scanner(System.in);
	public static void main(String[] args) {
		
		//生成一个地图对象
		Graph graph = new Graph();
		//建立地图
		createUDN(graph);
		printf("********************欢迎来到XX大学*********************");
		printf("*                                           		 *");
		printf("*             1.查询景点信息                          *");
		printf("*             2.问路查询                              *");
		printf("*             3.修改已有景点的相关信息			     *");
		printf("*             4.增加一个新景点及相关信息			     *");
		printf("*             5.增加一条新的路径					     *");
		printf("*             6.删除一个景点及其相关信息               *");
		printf("*             7.删除一个路径                          *");
		printf("*             8.退出                                  *");
		printf("*                                                     *");
		printf("********************XX大学导游系统*********************");
		while(true)
		{
			printf("请选择需要的服务(1-6):");
			int Select = input.nextInt();
			if(Select == 1)
			{
				printf("本校景点有:");
				for(int i = 0; i < graph.vexnum; i ++)
					printf("             " + (i+1) + "." + graph.vexs[i].name);
				printf("请选择您要查询的景点" + "(1-" + graph.vexnum + "):");
				int select = input.nextInt();
				printf(graph.vexs[select - 1].name + "  " + graph.vexs[select - 1].info);
			}
			else if(Select == 2)
			{
				printf("本校景点有:");
				for(int i = 0; i < graph.vexnum; i++)
					printf("             " + (i+1) + "." + graph.vexs[i].name);
				printf("请输入您的位置" + "(1-" + graph.vexnum + "):");
				int initialPosition = input.nextInt();
				printf("请输入您的目的地" + "(1-" + graph.vexnum + "):");
				int targetPosition = input.nextInt();
				ShortestPath_DIJ(graph,initialPosition,targetPosition);
			}
			else if(Select == 3)
			{
				printf("请输入您想修改哪个景点的相关信息" + "(1-" + graph.vexnum + "):");
				int number = input.nextInt();
				System.out.println("请输入该景点的相关信息:");
				String message = input.next();
				if(number < graph.vexnum + 1 && number > 0)
					graph.vexs[number - 1].info = message;
				else
					printf("对不起!您输入的标号有误!");
			}
			else if(Select == 4)
			{
				printf("请输入新景点的名字:");
				String name = input.next();
				printf("请输入新景点的相关信息:");
				String info = input.next();
				graph.vexnum++;
				graph.vexs[graph.vexnum - 1] = new VertexType(name,info);
				printf("添加景点成功!");
			}
			else if(Select == 5)
			{
				printf("请输入路径的始点" + "(1-" + graph.vexnum + "):");
				int initial = input.nextInt();
				printf("请输入路径的终点"  + "(1-" + graph.vexnum + "):");
				int destination = input.nextInt();
				printf("请输入始点和终点的距离:");
				int distance = input.nextInt();
				if((0 < initial && initial < graph.vexnum + 1) && (0 < destination && destination < graph.vexnum + 1))
				{
					graph.arcs[initial-1][destination-1] = distance;
					graph.arcs[destination-1][initial-1] = distance;
					printf("添加路径成功!");
				}
				else
					printf("对不起!您输入的位置有误!");
			}
			else if(Select == 6)
			{
				printf("请输入将要删除景点的标号" + "(1-" + graph.vexnum + "):");
				int number = input.nextInt();
				//在范围内
				if(0 < number && number < graph.vexnum + 1)
				{
					graph.vexs[number - 1] = null;
					for(int h = number-1; h < graph.vexnum; h++)
					{
						//把景点删除
						graph.vexs[h] = graph.vexs[h + 1];
					}
					for(int w = number - 1; w < graph.vexnum; w++)
						for(int j = 0; j < graph.arcs[w].length; j++)
						{
							//把路径删除
							graph.arcs[w][j] = graph.arcs[w + 1][j];
							graph.arcs[j][w] = graph.arcs[j][w+1];
						}
					graph.vexnum--;
					printf("删除景点完毕!");
				}
				else
					printf("对不起!您输入的标号有误!");
			}
			else if(Select == 7)
			{
				printf("请输入将要删除路径的始点标号" + "(1-" + graph.vexnum + "):");
				int initial = input.nextInt();
				printf("请输入将要删除路径的终点标号" + "(1-" + graph.vexnum + "):");
				int destination = input.nextInt();
				if((0 < initial && initial < graph.vexnum + 1) && (0 < destination && destination < graph.vexnum + 1))
				{
					graph.arcs[initial-1][destination-1] = MaxInt;
					graph.arcs[destination-1][initial-1] = MaxInt;
					printf("删除路径完毕!");
				}
				else
					printf("对不起!您输入的标号有误!");
			}
			else if(Select == 8)
			{
				printf("谢谢您的使用!欢迎下次再来!");
				break;
			}
			else
				printf("对不起，输入有误!请重新输入!");
		}
	}
	public static void printf(Object o)
	{
		System.out.println(o);
	}
	public static void createUDN(Graph graph)
	{
		try
		{
			String encoding="utf-8";
			//这个路径要及时修改!!!
            File file=new File("C:/Users/Lenovo/Desktop/Graph.txt");
            //判断文件是否存在
            if(file.isFile() && file.exists())
            {
            	//考虑到编码格式,这里用到utf-8，中文字符更多。
            	//将字节流输入流转换成字符输入流
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);
                //将字符输入流转换成带缓冲区的字符输入流
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int i = 1;
                int e,f;
                while((lineTxt = bufferedReader.readLine()) != null)
                {
                	String[] a = lineTxt.split(" ");
                	if(i == 1)
                	{
                		graph.vexnum = Integer.parseInt(a[0]);
                		graph.arcnum = Integer.parseInt(a[1]);
                	}
                	if(1 < i && i <= (graph.vexnum + 1))
                	{
                		graph.vexs[i-2] = new VertexType(a[0],a[1]);
                	}
                	if(i <= (graph.arcnum + graph.vexnum + 1) && i > (graph.vexnum + 1))
                	{
                		e = LocateVex(graph,a[0]);
                		f = LocateVex(graph,a[1]);
                		graph.arcs[e][f] = Integer.parseInt(a[2]);
                		graph.arcs[f][e] = Integer.parseInt(a[2]);
                	}
                	i++;
                }
                //关闭文件，关闭了read，其他也都关闭了
                read.close();
                for(int h = 0; h < graph.arcs.length; h++)
                	for(int j = 0; j < graph.arcs[h].length; j++)
                		if(h != j && graph.arcs[h][j] == 0)
                			graph.arcs[h][j] = MaxInt;
            }
		}catch(Exception e)
		{
			printf(e.getMessage());
		}
	}
	//查找顶点的位置
	public static int LocateVex(Graph graph,String name)
	{
		for(int i = 0; i < graph.vexnum; i++)
			if(name.equals(graph.vexs[i].name))
				return i;
		return -1;
	}
	public static void ShortestPath_DIJ(Graph graph,int v0,int v1)
	{
		int n = graph.vexnum;			//先把地图的位置顶点用n来表示
		boolean [] S = new boolean[n];	//每执行一次循环找出最小路径，如果找出一个最小路径就把该点置为true
		int [] D = new int[n];			//存权值(最小的),如果有更小的将会代替该位置上大的权值
		int [] Path = new int[n];		//记录该索引顶点的前驱顶点
		
		//初始化,先找出v0结点的邻居结点的各个权值
		for(int i = 0; i < n; i ++)
		{
			S[i] = false; 				//S中先都置为false
			D[i] = graph.arcs[v0 -1][i]; 	//最开始先把v0与其他几个顶点的权值赋值给D[i]
			if(D[i] != MaxInt && D[i] != 0)
				Path[i] = v0 - 1;			//第i个顶点的前驱结点就是v0,表示i和v0相邻
			else
				Path[i] = -1;			//表示i顶点和v0顶点没有相邻,如果相邻必有权值不为无穷大
		}
		S[v0 - 1] = true;					//v0与v0之间路径为0，我们不再讨论
		
		int v = - 1;
		//对剩下的n-1个顶点循环。
		for(int i = 1; i < n; i++)
		{
			int m = MaxInt;
			//找出D中最小的路径
			for(int w = 0; w < n; w++)
				if(!S[w] && D[w] < m)
				{
					m = D[w];			//依次比较D中的权值，找出最小的权值
					v = w;			//把这个最小权值的位置赋值给v(记录下来)
				}
			if(v != -1)				//表示我们找到了D中的最小路径
			{
				S[v] = true;		//我们找到了最小路径,我们把它置为true,以防下次还找到它
				for(int w = 0; w < n; w++)
					if(!S[w] && D[v] + graph.arcs[v][w] < D[w])		//如果原来的路径D[w](0-n)依次和v0到v的权值再加上v到w的权值进行比较
					{
						D[w] = D[v] + graph.arcs[v][w];				//如果小于了，等于说有最优路径,我们把最优路径赋值给D[w]
						Path[w] = v;								//并把w的前驱置为v
					}
			}			
		}
		int [] a = new int[n];
		for(int i = 0; i < Path.length; i++)
		{
			a[i] = -1;
			 if(Path[v1 - 1] == v0 - 1)
			 {
				 continue;
			 }
			 else
			 {
				 a[i] = Path[v1 - 1];
				 if(Path[v1 - 1] != -1)
					 Path[v1 - 1] = Path[Path[v1 - 1]];
			 }
		}
		if(D[v1 - 1] != MaxInt)
		{
			System.out.print("路径是:" + graph.vexs[v0 - 1].name);
			for(int i = a.length - 1; i >= 0; i --)
			{
				if(a[i] != -1)
				{
					System.out.print("->" + graph.vexs[a[i]].name);
				}
			}
			System.out.print("->" + graph.vexs[v1 - 1].name + "\n");
			printf("最短距离为:" + D[v1 - 1]);
		}
		else
			printf("对不起!两者不连通!");
	}
}