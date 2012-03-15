cd D:\PhDWork\Jspace\Mobilesink\test\new\ImpactPerformance\T200;

    %v = strcat(strcat('running_',int2str(i)),'.txt');
    Size=[100,200,300,400,500,600];
    C = load('unit-utilitygain-tour.txt');
    N = C(:,1);
    [m,n]=size(N);
    RB = C(:,4);
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    h=plot(N,RB,'-dr');
    %set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerFaceColor',[0.113 0.020 0.025],'MarkerSize',10);
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 400]);
    %legend('Garg and K.','TPath',2);
    
    xlabel('Network Size');
    %ylabel('total flow per second(byte)');
    ylabel('Network Goodput');
    %title('Flow Comparison');
    
    hold on;
    C = load('max-utilitygain-tour.txt');
    RB = C(:,4);
    h=plot(N,RB,'-ob');
    set(h,'MarkerSize',12);
    
    C = load('random-utilitygain-tour.txt');
    RB = C(:,4);
    h=plot(N,RB,'-sk');
    set(h,'MarkerSize',12);
    
    
    
    legend('Max\_UtilityGain','Max\_Utility','Random\_Utility',2);
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
    

