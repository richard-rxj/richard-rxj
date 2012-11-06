cd D:\PhDWork\Jspace\pathconstrained\test\matlab\mfiles;

    f = load('f-20-0.txt');
    b = load('b-20-0.txt');
    A = load('a-20-0.txt');
    beq = load('beq-20-0.txt');
    Aeq = load('aeq-20-0.txt');
    f=f'.*(-1);
    x=bintprog(f,A,b,Aeq,beq);
    r=f'*x*(-1);

    %v = strcat(strcat('running_',int2str(i)),'.txt');
    
    %for t=(1:1:m-1)
   %     str  = [ 'PRatio:',num2str(RB(t)*100),'%' ]; 
   %     text(N(t),WB(t),['\leftarrow',str]);
   % end
   % str  = [ 'PRatio:',num2str(RB(m)*100),'%' ]; 
   % text(N(m)-24,WB(m),[str,'\rightarrow']);
    %
  