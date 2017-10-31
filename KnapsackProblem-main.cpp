
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

// represents a menu
int menu(int kk)
{
	int c;
	while (kk) {
		cout << "Knapsack Problem：" << endl;
		cout << "1.Generate Food Randomly" << endl;
		cout << "2.Solve the problem in Greedy, Branch and Bound and Dynamic Programming" << endl;
		cout << "3.Branch and Bound" << endl;
		cout << "4.Greedy" << endl;
		cout << "5.Dynamic Programming        " << endl;
		cout << "---0.exit---" << endl << endl;
		cin >> c;
		switch (c)
		{
		case 1:GenerateFood(); break;
		case 2:BranchAndBound(); Greedy(); DynamicProgramming(); break;
		case 3:BranchAndBound(); break;
		case 4:Greedy(); break;
		case 5:DynamicProgramming(); break;
		case 0:kk = 0; break;
		default:
			cout << "Wrong Value" << endl << "Please choose a number between 0 to 5" << endl << endl; break;
		}
	}
	return 0;
}

// represents a main method
int main()
{
	menu(kk);
	return 0;
}

