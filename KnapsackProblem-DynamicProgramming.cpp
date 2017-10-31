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



void Knaspack2()
{
	int i, j;
	int Max = min(dw[dn] - 1, dW);
	for (int j = 1; j <= Max; j++)
		dV[dn][j] = 0;
	for (int j = dw[dn]; j <= dW; j++)
		dV[dn][j] = dv[dn];
	for (int i = dn - 1; i > 1; i--)
	{
		Max = min(dw[i] - 1, dW);
		for (j = 1; j <= Max; j++)
			dV[i][j] = dV[i + 1][j];
		for (j = dw[i]; j <= dW; j++)
			dV[i][j] = max(dV[i + 1][j], dV[i + 1][j - dw[i]] + dv[i]);
	}
	dV[1][dW] = dV[2][dW];
	if (dW > dw[1])
		dV[1][dW] = max(dV[1][dW], dV[2][dW - dw[1]] + dv[1]);
}


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



void Traceback()
{
	for (int i = 1; i < dn; i++)
	{
		if (dV[i][dW] == dV[i + 1][dW])
			dbest[i] = 0;
		else
		{
			dbest[i] = 1;
			dW -= dw[i];
		}
	}
	dbest[dn] = (dV[dn][dW]) ? 1 : 0;
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

// Solve the problem using dynamic programming
int DynamicProgramming()
{
	clock_t start, finish;
	double totaltime;
	start = clock();
	int i, j;
	ofstream fout("dt.txt");
	{
		ifstream file;
		goodsfz b[N];
		int LINES;
		string filename = "output.txt";
		char buffer[256];
		file.open(filename, ios::in);
		if (file.fail())
		{
			cout << "File doesn't exit" << endl;
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

				dw[i] = tc[i]; dv[i] = t[i];

			}
			Knaspack2();
			Traceback();
			int totalW = 0;
			int totalV = 0;
			printf("KnapsackProblem using dynamic programing: ");
			fout << "KnapsackProblem using dynamic programing: ";

			cout << "Selected Foods" << endl;
			cout << "Food Index:" << setw(5) << " Weight:" << setw(5) << "Price:" << endl;
			for (i = 1; i <= dn; i++)
			{
				if (dbest[i] == 1)
				{
					totalW += dw[i];
					totalV += dv[i];
					cout << setiosflags(ios::left) << setw(12) << i + 1 << setw(10) << dw[i] << setw(3) << dv[i] << endl;
				}
			}
			cout << "Total Weight:" << totalW << "  Maximum Value:" << " " << totalV << endl;

		}

		finish = clock();
		totaltime = (double)(finish - start) / CLOCKS_PER_SEC;
		cout << "\nRun time of Dynamic Programming" << totaltime << "s" << endl;
		fout << "\nRun time of Dynamic Programming" << totaltime << "s" << endl;
		cout << "==========================================" << endl << endl << endl;
		fout << flush; fout.close();
		return 0;
	}
}

