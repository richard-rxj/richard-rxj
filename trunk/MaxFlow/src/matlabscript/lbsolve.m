cd D:\PhDWork\Jspace\MaxFlow\test\RandomNoWeight\MatlabLP;
conSet = load('test-config.txt');

for i=conSet
    f = strcat(strcat('a',int2str(i)),'.txt');
    A = load(f);
    f = strcat(strcat('f',int2str(i)),'.txt');
    F = load(f);
    f = strcat(strcat('b',int2str(i)),'.txt');
    B = load(f);
    f = strcat(strcat('lb',int2str(i)),'.txt');
    LB = load(f);
    X = linprog(F,A,B,[],[],LB);
    [x,y]= size(A);
    f = strcat(strcat('Matlab_Random_Rate_',int2str(i)),'.txt');
    fid=fopen(f,'wt+');
    for j=1:1:i-1
        r = A(1,1:y-1)*X(1:y-1,1);
        fprintf(fid,'%f %f\n',j,-1*r);
    end
    fclose(fid);
end
