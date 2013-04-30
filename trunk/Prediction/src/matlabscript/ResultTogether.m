cd F:\PhDWork\papers\richard\prediction\NSRDB\data;
    
    trb=2;   %max
    %trb=8;   %dis
    
    

    C = load('result.txt');
    N = C(:,1);
    RB = C(:,2);
    h=plot(N,RB,'-r');
    %set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerFaceColor',[0.113 0.020 0.025],'MarkerSize',10);
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 400]);

    
    xlabel('Hours');

    ylabel('Amount of the energy (mW)');

    
    hold on;
   
    RB = C(:,3);
    h=plot(N,RB,':b');
    set(h,'MarkerSize',12);
    
    RB = C(:,5);
    h=plot(N,RB,'--k');
    set(h,'MarkerSize',12);
    
%     RB = C(:,7);
%     h=plot(N,RB,'-^m');
%     set(h,'MarkerSize',12);
    

%     RB = C(:,trb);
%     h=plot(N,RB,'-xm');
%     set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerSize',12);
%     set(h,'MarkerSize',12);
%     
% 
%     RB = C(:,trb);
%     h=plot(N,RB,'-pm');
%     set(h,'MarkerSize',12);
%     set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    
    
    
    %axis([0 250 0 500]);
    legend('Real','EWMA','VEWMA','WCMA',2);
  
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);

