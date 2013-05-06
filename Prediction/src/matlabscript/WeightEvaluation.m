cd F:\PhDWork\papers\richard\prediction\NSRDB\data;
    
    trb=11;   %max
    %trb=8;   %dis
    
    

    C = load('error.txt');
    N = C(1:trb,1);
    RB = C(1:trb,2);
    h=plot(N,RB,'-dr');
    %set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerFaceColor',[0.113 0.020 0.025],'MarkerSize',10);
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 400]);



    
    hold on;
   
    RB = C(1:trb,3);
    h=plot(N,RB,'-sb');
    set(h,'MarkerSize',12);
    
    RB = C(1:trb,4);
    h=plot(N,RB,'-ok');
    set(h,'MarkerSize',12);
    
    RB = C(1:trb,5);
    h=plot(N,RB,'-^m');
    set(h,'MarkerSize',12);
    

    RB = C(1:trb,6);
    h=plot(N,RB,'-xm');
    %set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerSize',12);
    set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    set(h,'MarkerSize',12);
%     
% 
%     RB = C(:,trb);
%     h=plot(N,RB,'-pm');
%     set(h,'MarkerSize',12);
%     set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    
    xlabel('w');

    ylabel('Error');
    
    axis([0.1 0.99 0 0.18]);
    legend('DataSet1','DataSet2','DataSet3','DataSet4','DataSet5',3);
    
    set(gca,'XTick',[0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,0.99]);
    xTL={'0.1', '0.2', '0.3', '0.4', '0.5', '0.6', '0.7', '0.8', '0.9', '0.99'};
     
    set(gca,'XTickLabels',xTL);

    
    
  
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);

