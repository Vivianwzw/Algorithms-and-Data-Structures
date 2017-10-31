// BranchAndBound.cpp : Solve the knapsack problem in branch and bound algorithm.

#include <iostream>
#include <stdio.h>
#include <stdlib.h> 
#include <time.h>
#include <iomanip>
#include <fstream>
#include <string>
#include<algorithm>
#define N 100

using namespace std;

int kk = 1; // control for menu
int n, fn, tn, hn, dn;// amount of food
int m, limitw, dW, hc;// the largest amount of food that a plate can take
int  C, bestP = 0, cp = 0, cw = 0;
int X[N], cx[N];
int dw[N], dv[N], dbest[N];
int dV[N][N];  // the greatest value matrix

// a structure for goods.
// generate food structure randomly
struct goods {
	int w; // the weight of food
	int v; // the value of food
}a[N];

// a structure for branch and bound algo.
struct goodsfz	//
{
	int sign;	// the number of food
	int w;	//the weight of food
	int p;	//the value of food
}fz[N];

//read the file
int CountLines(string filename)// access the length of the file
{
	ifstream ReadFile;
	int n = 0;
	string temp;
	ReadFile.open(filename, ios::in);// read the file in read only mode
	if (ReadFile.fail())
	{
		return 0;
	}
	else
	{
		while (getline(ReadFile, temp))
		{
			n++;
		}
		return n;
	}
	ReadFile.close();
}


// helper for branch and bound algorithm
bool fzm(goodsfz a, goodsfz b)
{
	return (a.p / a.w)>(b.p / b.w);
}

int max(int a, int b)
{
	return a<b ? b : a;
}

// a structure for the branch and bound status.
struct KNAPNODE	
{
	bool s1[N]; 
	int k;		
	int b;	
	int w;	
	int p;	
};

// a struct for heap
struct HEAP	
{
	KNAPNODE *p;
	int b;        
};

// switch two elements
void swap(HEAP &a, HEAP &b)
{
	HEAP temp = a;
	a = b;
	b = temp;
}
// move up the node
void mov_up(HEAP H[], int i)
{
	bool done = false;
	if (i != 1) {
		while (!done && i != 1) {
			if (H[i].b>H[i / 2].b) {
				swap(H[i], H[i / 2]);
			}
			else {
				done = true;
			}
			i = i / 2;
		}
	}
}
//move down the node
void mov_down(HEAP H[], int fn, int i)
{
	bool done = false;
	if ((2 * i) <= fn) {
		while (!done && ((i = 2 * i) <= fn)) {
			if (i + 1 <= fn && H[i + 1].b > H[i].b) {
				i++;
			}
			if (H[i / 2].b<H[i].b) {
				swap(H[i / 2], H[i]);
			}
			else {
				done = true;
			}
		}
	}
}
//insert a node
void insert(HEAP H[], HEAP x, int &fn)
{
	fn++;
	H[fn] = x;
	mov_up(H, fn);
}
// delete a node
void del(HEAP H[], int &fn, int i)
{
	HEAP x, y;
	x = H[i]; y = H[fn];
	fn--;
	if (i <= fn) {
		H[i] = y;
		if (y.b >= x.b) {
			mov_up(H, i);
		}
		else {
			mov_down(H, fn, i);
		}
	}
}
//delete the top node
HEAP del_top(HEAP H[], int &fn)
{
	HEAP x = H[1];
	del(H, fn, 1);
	return x;
}
//calculate the bound.
void bound(KNAPNODE* node, int M, goodsfz fz[], int fn)
{
	int i = node->k;
	float w = node->w;
	float p = node->p;
	if (node->w>M) {    
		node->b = 0;    
	}
	else {
		while ((w + fz[i].w <= M) && (i<fn)) {
			w += fz[i].w;   
			p += fz[i++].p;  
		}
		if (i<n) {
			node->b = p + (M - w)*fz[i].p / fz[i].w;
		}
		else {
			node->b = p;
		}
	}
}

int KnapSack4(int fn, goodsfz fz[], int C, int X[])
{
	int i, k = 0;      
	int v;
	KNAPNODE *xnode, *ynode, *znode;
	HEAP x, y, z, *heap;
	heap = new HEAP[fn*fn];         
	for (i = 0; i<fn; i++) {
		fz[i].sign = i;       
	}
	sort(fz, fz + fn, fzm);              
	xnode = new KNAPNODE;         
	for (i = 0; i<fn; i++) {            
		xnode->s1[i] = false;
	}
	xnode->k = xnode->w = xnode->p = 0;
	while (xnode->k<fn) {
		ynode = new KNAPNODE;      
		*ynode = *xnode;          
		ynode->s1[ynode->k] = true;     
		ynode->w += fz[ynode->k].w;    
		ynode->p += fz[ynode->k].p;     
		ynode->k++;                
		bound(ynode, C, fz, fn);  
		y.b = ynode->b;
		y.p = ynode;
		insert(heap, y, k);        
		znode = new KNAPNODE;      
		*znode = *xnode;           
		znode->k++;                         
		bound(znode, C, fz, fn);  
		z.b = znode->b;
		z.p = znode;
		insert(heap, z, k);      
		delete xnode;
		x = del_top(heap, k);    
		xnode = x.p;
	}
	v = xnode->p;
	for (i = 0; i<fn; i++) {      
		if (xnode->s1[i]) {
			X[fz[i].sign] = 1;
		}
		else {
			X[fz[i].sign] = 0;
		}
	}
	delete xnode;
	delete heap;
	return v;              
}


int min(int x, int y)
{
	return x >= y ? y : x;
}

int GenerateFood()
{
	ofstream fout("output.txt");
	srand((unsigned)time(NULL));
	int i;
	cout << "Total Amount of food： " << endl;
	cin >> n;
	hn = n; dn = n; tn = n; fn = n;
	cout << "Available space of plate： " << endl;
	cin >> m;
	C = m; limitw = m; dW = m; hc = m;
	for (i = 0; i < n; ++i)
	{

		a[i].w = rand() % (m - 1) + 1;

		fout << a[i].w << setw(5) << " ";
		a[i].v = rand();

		fout << a[i].v << endl;

	}
	fout << flush;
	fout.close();
	return 0;
}

int BranchAndBound()
{
	clock_t start, finish;
	double totaltime;
	start = clock();
	ofstream fout("fz.txt");
	{
		ifstream file;
		goodsfz b[N];
		int LINES;

		string filename = "output.txt";
		char buffer[256];

		file.open(filename, ios::in);
		if (file.fail())
		{
			cout << "File doesn't exist" << endl;
			file.close();
			cin.get();
			cin.get();
		}
		else
		{

			LINES = CountLines(filename);
			int *tc = new int[LINES];
			int *t = new int[LINES];
			int i = 0;
			while (!file.eof())
			{
				file >> tc[i];
				file >> t[i];
				i++;
			}
			file.close();
			for (i = 0; i < LINES; i++)
			{
				fz[i].w = tc[i]; fz[i].p = t[i]; b[i] = fz[i];
			}
			int sum4 = KnapSack4(fn, b, C, X);
			printf("Knapsack problem using branch and bound:\nX=[ ");
			fout << "Knapsack problem using branch and bound:\nX=[";
			for (int i = 0; i < fn; i++)
			{
				cout << X[i] << " ";
				fout << X[i] << " ";
			}
			printf("]	Value of food%d\n", sum4);
			fout << "]	Value of food" << sum4 << endl;
		}
	}

	finish = clock();
	totaltime = (double)(finish - start) / CLOCKS_PER_SEC;
	cout << "\nRunTime using branch and bound" << totaltime << "s" << endl;
	fout << "\nRunTime using branch and bound" << totaltime << "s" << endl;

	cout << "==========================================" << endl << endl << endl;
	fout << flush; fout.close();
	return 0;
}
