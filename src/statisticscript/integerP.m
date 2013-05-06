cd D:\PhDWork\Jspace\pathconstrained\test\matlab;

   node=[20 30 40 50 60];  %nodeset
   cs=10;                  %cishu
   
   fid = fopen('finaldata.txt', 'w');



for i = 1:length(node)
    r=0;
    fprintf(fid, '%i\t', node(i));
    
    for j = 1:1:cs
        
        fFile = ['f-',int2str(node(i)),'-',int2str(j-1),'.txt'];
        f = load(fFile);
        bFile = ['b-',int2str(node(i)),'-',int2str(j-1),'.txt'];
        b = load(bFile);
        AFile = ['a-',int2str(node(i)),'-',int2str(j-1),'.txt'];
        A = load(AFile);
        beqFile = ['beq-',int2str(node(i)),'-',int2str(j-1),'.txt'];
        beq = load(beqFile);
        AeqFile = ['aeq-',int2str(node(i)),'-',int2str(j-1),'.txt'];
        Aeq = load(AeqFile);
        x=bintprog(f,A,b,Aeq,beq);
        tr=f'*x*(-1);     
        r=r+tr;
    end
     fprintf(fid, '%f\t%s\n', r/cs);
end

fclose(fid);




%     f = load('f-20-0.txt');
%     b = load('b-20-0.txt');
%     A = load('a-20-0.txt');
%     beq = load('beq-20-0.txt');
%     Aeq = load('aeq-20-0.txt');
%     f=f'.*(-1);
%     x=bintprog(f,A,b,Aeq,beq);
%     r=f'*x*(-1);

   
  