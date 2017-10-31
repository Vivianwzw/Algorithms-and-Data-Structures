

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


//sort the food by value per unit weight
void sort1(int n, float a[N], float b[N])
{
	int j, h, k;
	float t1, t2, t3, c[N];
	for (k = 0; k<n; k++)
		c[k] = a[k] / b[k];
	for (j = 0; j<n; j++)
		if (c[j]<c[j + 1])
		{
			t1 = a[j]; a[j] = a[j + 1]; a[j + 1] = t1;
			t2 = b[j]; b[j] = b[j + 1]; b[j + 1] = t2;
			t3 = c[j]; c[j] = c[j + 1]; c[j + 1] = t3;
		}
}

// helper for greedy()
void knapsack1(int n, float limitw, float v[N], float w[N], int x[N])
{
	float c1;
	int i;
	sort1(n, v, w);
	c1 = limitw;
	for (i = 0; i<n; i++)
	{
		if (w[i]>c1)break;
		x[i] = 1;
		c1 = c1 - w[i];
	}
}



// solving the problem using greedy algorithm
int Greedy()
{
	clock_t start, finish;
	double totaltime;
	start = clock();
	int  i, x[N];
	float v[N], w[N], totalv = 0, totalw = 0;
	ofstream fout("tx.txt");
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
				x[i] = 0;
				w[i] = tc[i]; v[i] = t[i];

			}
			knapsack1(tn, limitw, v, w, x);
			printf("Knapsack problem using Greedy Algorithm: ");
			fout << "Knapsack problem using Greedy Algorithm:";
			cout << "Selected food are";
			for (i = 0; i<tn; i++)
			{
				cout << x[i];
				if (x[i] == 1) {
					totalw = totalw + w[i];
					totalv = totalv + v[i];
				}
			}
			cout << endl;
			cout << "Total Weight：" << totalw << endl;
			cout << "Total Value：" << totalv << endl;
			fout << "Total Weight：" << totalw << endl << "Total Value：" << totalv << endl;
		}

	}

	finish = clock();
	totaltime = (double)(finish - start) / CLOCKS_PER_SEC;
	cout << "\nRun Time for Greedy Algorithm" << totaltime << "s" << endl;
	fout << "\nRun Time for Greedy Algorithm" << totaltime << "s" << endl;

	cout << "==========================================" << endl << endl << endl;
	fout << flush; fout.close();
	return 0;
}
