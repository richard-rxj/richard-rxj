

    %v = strcat(strcat('running_',int2str(i)),'.txt');

    x = 0:0.1:1;

    %RB = (1.1).^(1-x);
    RB = 1-(1-x).^(1.1);
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    h=plot(x,RB,'-dr');
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 600]);
    %legend('Garg and K.','TPath',2);
    
    xlabel('x');
    %ylabel('total flow per second(byte)');
    ylabel('y = 1-(1-x)^a');
    %title('Flow Comparison');
    
    hold on;

%      %RB = 2.^(1-x);
%     RB = 1-(1-x).^(1.5);
%     h=plot(x,RB,'->m');
%     set(h,'MarkerSize',12);
    
    
    %RB = 2.^(1-x);
    RB = 1-(1-x).^2;
    h=plot(x,RB,'-ob');
    set(h,'MarkerSize',12);
    
  
    %RB = 4.^(1-x);
    RB = 1-(1-x).^8;
    h=plot(x,RB,'-^k');
    set(h,'MarkerSize',12);
    
  
    %RB = 8.^(1-x);
    RB = 1-(1-x).^32;
    h=plot(x,RB,'-pm');
    set(h,'MarkerSize',12);
    
    
     %RB = 16.^(1-x);
    RB = 1-(1-x).^100;
    h=plot(x,RB,'-sm');
    set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    
%     C = load('linear-time-4800.txt');
%     RB = C(:,2);
%     h=plot(N,RB,'-sb');
%     set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
%     
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
    
     legend('a = 1.1','a = 2','a = 8','a = 32','a = 100',4);

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
    x=[0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1];
    %set(gca,'xtick',xt);
    %set(gca,'xticklabel',xt);
    set(gca, 'XTick', [0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1]);
   set(gca,'XTickLabel',{'0' '0.1' '0.2' '0.3' '0.4' '0.5' '0.6' '0.7' '0.8' '0.9' '1'});
    set(gca, 'YTick', [0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1]);
   set(gca,'YTickLabel',{'0' '0.1' '0.2' '0.3' '0.4' '0.5' '0.6' '0.7' '0.8' '0.9' '1'});
   
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);
    %v='pratio';
    %saveas(gcf,v,'eps');
    

