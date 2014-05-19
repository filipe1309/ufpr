#include"prototipos.h"
int rotulacao(int medoids[],img_struct nova_img,int tam,int *rot)
{
	int i=0 ,j;
	int custo = 0, dist;
	while(i<nova_img.altura)
	{
		j=0;
		while(j<nova_img.largura)
		{
			rot[j + i*nova_img.largura]=escolhe_caso(medoids,nova_img.pontos[j + i*nova_img.largura],tam,&dist);/*escolhe a que grupo pertence a posição atual*/
			custo+=dist;
			j++;
		}
		i++;
	}
	return custo;
}
