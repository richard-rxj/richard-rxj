cd D:\PhDWork\Jspace\Mobilesink\test\ImpactTime;

    %v = strcat(strcat('running_',int2str(i)),'.txt');
    Size=[100,200,300,400,500,600];
    C = load('linear-time-300.txt');
    N = C(:,1);
    [m,n]=size(N);
    RB = C(:,2);
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    h=plot(N,RB,'-dr');
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 400]);
    %legend('Garg and K.','TPath',2);
    
    xlabel('Network Size');
    %ylabel('total flow per second(byte)');
    ylabel('Network Throughput (bit)');
    %title('Flow Comparison');
    
    hold on;
    C = load('linear-time-600.txt');
    RB = C(:,2);
    h=plot(N,RB,'-ob');
    set(h,'MarkerSize',12);
    
    C = load('linear-time-1200.txt');
    RB = C(:,2);
    h=plot(N,RB,'-^k');
    set(h,'MarkerSize',12);
    
    C = load('linear-time-2400.txt');
    RB = C(:,2);
    h=plot(N,RB,'-pm');
    set(h,'MarkerSize',12);
    
    
    C = load('linear-time-4800.txt');
    RB = C(:,2);
    h=plot(N,RB,'-sb');
    set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    
%     C = load('linear-time-36000.txt');
%     RB = C(:,2);
%     plot(N,RB,'->b');
%     
%     C = load('linear-time-72000.txt');
%     RB = C(:,2);
%     plot(N,RB,'-<b');
%     
%     C = load('linear-time-144000.txt');
%     RB = C(:,2);
%     plot(N,RB,'-hb');
%     
    
     legend('T = 300','T = 600','T = 1200','T = 2400','T = 4800',2);

%    legend('T = 300','T = 600','T = 1200','T = 2400','T = 4800','T = 36000','T = 72000','T = 144000',2);
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
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);
    %v='pratio';
    %saveas(gcf,v,'eps');
    

