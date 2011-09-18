cd D:\PhDWork\Jspace\MaxFlowSVN\test\simulation\matlab\3-3\20;
%conSet = load('test-config.txt');

for i=1
    f = 'a.txt';
    A = load(f);
    f = 'f.txt';
    F = load(f);
    f = 'b.txt';
    B = load(f);
    f = 'lb.txt';
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
