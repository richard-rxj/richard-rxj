cd D:\PhDWork\Jspace\globecom2013\test\ImpactPerformance(no enough data);
    
    %trb=2;   %max
    trb=7;   %dis
    
    

    C = load('T100.txt');
    N = C(:,1);
    RB = C(:,trb);
    h=plot(N,RB,'-dr');
    %set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerFaceColor',[0.113 0.020 0.025],'MarkerSize',10);
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 400]);

    
    xlabel('Network Size');

    ylabel('Network Data Quality');

    
    hold on;
   
    C = load('T200.txt');
    RB = C(:,trb);
    h=plot(N,RB,'-ob');
    set(h,'MarkerSize',12);
    
    C = load('T400.txt');
    RB = C(:,trb);
    h=plot(N,RB,'-sk');
    set(h,'MarkerSize',12);
    
    C = load('T800.txt');
    RB = C(:,trb);
    h=plot(N,RB,'-^m');
    set(h,'MarkerSize',12);
    
    C = load('T3200.txt');
    RB = C(:,trb);
    h=plot(N,RB,'-xm');
    set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerSize',12);
    set(h,'MarkerSize',12);
    
    C = load('T6400.txt');
    RB = C(:,trb);
    h=plot(N,RB,'-pm');
    set(h,'MarkerSize',12);
    set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    
    
    
    axis([100 1000 0 500]);
    legend('T = 100','T = 200','T = 400','T = 800','T = 3200','T = 6400',2);
  
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);

