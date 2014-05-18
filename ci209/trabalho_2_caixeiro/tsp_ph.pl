% travelling salesman problem            Pascal Hitzler
%
% Starting node is '1'
%
% SWI-Prolog
%
% edges of the graph are given in the form A-D-B where the edge runs 
% from A to B with distance D
%
% the program computes the shortest route from 1 to 1 visiting all nodes
%
% call with ?- tsp(Vertices, Edges,Route, Distance).


% some data for testing, call eg. ?- data(2,V,E),tsp(V,E,Route,Sum).

data(1,[1,2,3],[1-1-2,2-5-1,2-2-3,3-1-2,1-4-3,3-3-1]).
data(2,[1,2,3,4,5],[1-4-2,2-1-1,1-4-3,3-1-1,2-5-3,3-2-2,1-2-4,4-2-1,2-5-4,
        4-4-2,3-4-4,4-1-3,1-1-5,5-1-1,2-3-5,5-2-2,3-0-5,5-1-3,4-9-5,5-8-4]).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% main routine:
%
% V: List of all the vertices (including the starting node 1
% E: List of all (directed) edges with distance (cf. above)
% Route: List containing the visited nodes (in reverse order)
% Sum: Distance to be travelled by that route
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


tsp(V,E,Route,Sum):-
        setof(ARoute-ASum,route(V,E,ARoute,ASum),Routes), %compute all routes
        min(Routes,Route-Sum).  %find minimal route
              
%find minimal route              
              
min([H|T],X):-
        min0(T,H,X).    %H is minimum of List without tail T.     
        
min0([],H,H).           %actual minimum is overall minimum
min0([H-Hs|T],_-As,X):- %actual minimum is greater than new head:
        As >= Hs,
        min0(T,H-Hs,X). %new head becomes actual minimum
min0([_-Hs|T],A-As,X):- %other case
        As =< Hs,
        min0(T,A-As,X). %keep actual minimum

%%%%%%%%%%%%%%%%%%%%%%        
% main subroutine
%
% variables as in tsp/4
%
% selects one route and computes distance. 
%
% Note: predicate is generative                    
%%%%%%%%%%%%%%%%%%%%%%   

route(V,E,Route,Sum):-
        select(V,1,V1),                         %remove start node
        visit_all(1,V1,E,[1],Route0,0,Sum0),    %visit all other nodes
        Route0=[H|_],
        member(H-D-1,E),                        %return to node 1
        Route=[1|Route0],
        Sum is D+Sum0.
        
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%        
% main subsubroutine
%
% visit all nodes and compute sum
%
% N: actual node
% V: remaining vertices
% E: list of all edges
% Rin: route to this point
% Rout: final route
% Sumin: actual distance
% Sumout: final distance       
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        
visit_all(_,[],_,R,R,Sum,Sum).                  %base case: no vertices left
visit_all(N,V,E,Rin,Rout,Sumin,Sumout):-        
        select(V,N1,V1),                        %select a vertice
        member(N-D-N1,E),                       %find edge (get distance)
        Sumout0 is Sumin+D,                     %update distance-sum
        visit_all(N1,V1,E,[N1|Rin],Rout,Sumout0,Sumout). %keep going
                                        
                
                
        
