CC=nvcc -arch sm_20
SRC=$(wildcard *.cu)
HEADERS = prototipos.h
EXEC=segmenta

tudo: $(EXEC)

segmenta: $(SRC) $(HEADERS)
	$(CC) -o $@ $^
	mv $(EXEC) ../bin

.PHONY: faxina limpa	

limpa: 	
	rm -rf *.∼* *.bak core*

faxina: limpa
	rm -rf ../bin/$(EXEC) *.o 
