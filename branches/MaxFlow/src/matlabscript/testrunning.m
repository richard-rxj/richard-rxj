cd D:\PhDWork\Jspace\MaxFlowSVN\test;
for i=[30]
    v = strcat(strcat('running_',int2str(i)),'.txt');
    C = load(v);
    N = C(:,1);
    [m,n]=size(N);
    
    
    
    CR = C(:,2);
    WR = C(:,5);
    RR = C(:,9);
    %subplot(1,2,2);
    plot(N,RR,'-*r');
    set(gcf,'Position',[1 1 670 330]);
    %legend('Garg and K.','TPath',2);
    xlabel('Number of Nodes');
    %ylabel('running time(ms)');
    ylabel('RRatio')
    %title('Running Time Comparison');
    %for t=(1:1:m-1)
    %    str  = [ 'RRatio',num2str(RR(t)*100),'%' ]; 
    %    text(N(t),WR(t),['\leftarrow',str]);
    %end
    %str  = [ 'RRatio',num2str(RR(m)*100),'%' ]; 
    %text(N(m)-23,WR(m),[str,'\rightarrow']);
    
    hold on;
    C = load('running_25.txt');
    RR = C(:,9);
    plot(N,RR,'-pb');
    
    C = load('running_20.txt');
    RR = C(:,9);
    plot(N,RR,'-ok');
    
    legend('\epsilon = 0.3','\epsilon = 0.25','\epsilon = 0.2',2);
end
