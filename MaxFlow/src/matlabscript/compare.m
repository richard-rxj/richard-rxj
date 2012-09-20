cd D:\PhDWork\Jspace\MaxFlow\test\comparison\T-0829;
conSet = load('test-config.txt');

for i = conSet(1:3)
    p = 1;
    for j = [200,100,50]
        f = strcat(strcat(strcat(strcat('Concurrent_Random_rate_',int2str(i)),'_'),num2str(j)),'.txt');
        C = load(f);
        [aX,aY]=size(C);
        N = C(:,1);
        CR = C(:,2);
        MM = C(:,3);
        f = strcat(strcat(strcat(strcat('DWF_Random_rate_',int2str(i)),'_'),num2str(j)),'.txt');
        W = load(f);
        WR = W(:,2);
        f = strcat(strcat('Matlab_Random_rate_',int2str(i)),'.txt');
        M = load(f);
        MR = M(:,2);
        subplot(1,3,p);
        plot(N,MR,'g',N,CR,'r',N,WR,'b');
        legend('Matlab','Garg','TPath',1);
        %plot(N,CR,'r',N,WR,'b');
        xlabel('Node ID');
        ylabel('Rate');
        axis([0,aX,0,MM(1,1)/2]);
        title(strcat(strcat(strcat(int2str(i),'Nodes-\epsilon'),num2str(j/1000)), '-Compare'));
        %legend('Garg','TPath',1);
        v = strcat(strcat(strcat('compareN',int2str(i)),'A'),num2str(j));
        %saveas(gcf,v,'pdf');
        p = p+1;
    end
    v = strcat('compare',int2str(i));
    saveas(gcf,v,'pdf');
end
