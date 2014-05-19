#include"prototipos.h"
int escolhe_caso(int centro[],int x,int tam, int *dist)
{
	int i, ind = 0, dist1, dist2;
	
	dist1 = fabs(centro[0] - x);
	for( i = 1; i < tam; i++)
	{	
		dist2 = fabs(centro[i] - x);
		if(dist2 < dist1)
		{
			dist1 = dist2;
			ind = i;
		}
	}
	*dist = dist1;
	return centro[ind];
}
