cd D:\PhDWork\Jspace\MaxFlowSVN\test\simulation\0-3;
for i=[30]
    v = strcat(strcat('running_',int2str(i)),'.txt');
    C = load(v);
    N = C(:,1);
    [m,n]=size(N);
    RB = C(:,2);
    WB = C(:,4);
    %subplot(1,2,1);
    plot(N,RB,'-*r',N,WB,'-pb');
    %plot(N,RB,'-*r');
    set(gcf,'Position',[1 1 670 330]);
    
    
    xlabel('Number of Nodes');
    %ylabel('total flow per second(byte)');
    ylabel('Running Time (ms)');
    %title('Flow Comparison');
    
    hold on;
    %C = load('running_20.txt');
    %RB = C(:,2);
    %plot(N,RB,'-pb');
    
    %C = load('running_10.txt');
    %RB = C(:,2);
    %plot(N,RB,'-ok');
    
    legend('Garg and K.','TPath',2);
    %legend('\epsilon = 0.3','\epsilon = 0.2','\epsilon = 0.1',2);
    %
    %for t=(1:1:m-1)
   %     str  = [ 'PRatio:',num2str(RB(t)*100),'%' ]; 
   %     text(N(t),WB(t),['\leftarrow',str]);
   % end
   % str  = [ 'PRatio:',num2str(RB(m)*100),'%' ]; 
   % text(N(m)-24,WB(m),[str,'\rightarrow']);
    %
   % set(gcf, 'PaperPositionMode', 'manual');
   % set(gcf, 'PaperUnits', 'inches');
   % set(gcf, 'PaperPosition', [2 1 4 2]);
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',16);
    set(get(gca,'ylabel'),'fontsize',16);
    set(get(gca,'title'),'fontsize',16);
    %v='pratio';
    %saveas(gcf,v,'eps');
    
end
