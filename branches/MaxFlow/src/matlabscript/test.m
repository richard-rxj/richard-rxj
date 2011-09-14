cd D:\PhDWork\Jspace\MaxFlowSVN\test;
for i=[30]
    v = strcat(strcat('running_',int2str(i)),'.txt');
    C = load(v);
    N = C(:,1);
    [m,n]=size(N);
    CB = C(:,4);
    WB = C(:,7);
    RB = C(:,8);
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    plot(N,RB,'-*r');
    set(gcf,'Position',[1 1 670 330]);
    %legend('Garg and K.','TPath',2);
    
    xlabel('Number of Nodes');
    %ylabel('total flow per second(byte)');
    ylabel('FRatio');
    %title('Flow Comparison');
    
    hold on;
    C = load('running_25.txt');
    RB = C(:,8);
    plot(N,RB,'-pb');
    
    C = load('running_20.txt');
    RB = C(:,8);
    plot(N,RB,'-ok');
    
    legend('\epsilon = 0.3','\epsilon = 0.25','\epsilon = 0.2',2);
    %
    %for t=(1:1:m-1)
   %     str  = [ 'PRatio:',num2str(RB(t)*100),'%' ]; 
   %     text(N(t),WB(t),['\leftarrow',str]);
   % end
   % str  = [ 'PRatio:',num2str(RB(m)*100),'%' ]; 
   % text(N(m)-24,WB(m),[str,'\rightarrow']);
    %
    set(gcf, 'PaperPositionMode', 'manual');
    set(gcf, 'PaperUnits', 'inches');
    set(gcf, 'PaperPosition', [2 1 4 2]);

    %v='pratio';
    %saveas(gcf,v,'eps');
    
end
