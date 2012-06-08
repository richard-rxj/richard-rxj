cd D:\PhDWork\Jspace\MaxFlow2012\test\data\utility;

    v = [ 'utility-C8-A100.txt' ];
    C = load(v);
    N = C(:,1);
    RB = C(:,2);
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    
    h1=plot(N,RB,'-vr');
    set(h1,'LineWidth',2,'MarkerSize',10);
    hold on;  
    
    %v = [ 'utility-C6-R80-A100.txt' ];
    C = load(v);
    RB = C(:,3);
    h2=plot(N,RB,'-ok');
    set(h2,'LineWidth',2,'MarkerSize',12);
    
    set(gcf,'Position',[1 1 600 400]);
    %legend('Garg and K.','TPath',2);
    
    xlabel('\it{Number of Nodes n}');
    %ylabel('total flow per second(byte)');
    ylabel('\it{Network Utility}');
    %title('Flow Comparison');
     
    
    %v = [ 'utility-C6-R40-A100.txt' ];
    C = load(v);
    RB = C(:,5);
    h3=plot(N,RB,'-pb');
    set(h3,'LineWidth',2,'MarkerSize',10);
    
    %v = [ 'utility-C6-R20-A100.txt' ];
    C = load(v);
    RB = C(:,6);
    h4=plot(N,RB,'-x');
    set(h4,'LineWidth',2,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerFaceColor',[0.113 0.020 0.025],'MarkerSize',12);
    
    
    %v = [ 'utility-C6-R0-A100.txt' ];
    C = load(v);
    RB = C(:,7);
    h6=plot(N,RB,'-^m');
    set(h6,'LineWidth',2,'MarkerSize',10);
    
    
    %v = [ 'utility-C6-R110-A100.txt' ];
    C = load(v);
    RB = C(:,8);
    h7=plot(N,RB,'-+');
    set(h7,'LineWidth',2,'Color',[0.13 0.0 0.0],'MarkerEdgeColor',[0.13 0.0 0.0],'MarkerFaceColor',[0.13 0.0 0.0],'MarkerSize',10);
   
    
%     [legh,objh,outh,outm]=legend([h1,h2,h3],'w=1.0','w=0.8','w=0.4','Location','north');
%     legend boxoff;
%     legh2=copyobj(legh,gcf);
%     [legh2,objh2]=legend([h4,h6,h7],'w=0.2','w=0.0','variable','--','Location','northeast'); 
%     legend boxoff;
%     
    
    
   
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
   axis([50 500 40 450]);
   
   set(gca, 'YTick', [50 100 150 200 250 300 350 400 450]);
   set(gca,'YTickLabel',{'50','100','150','200','250','300','350','400','450'})
   
   
    
    set(get(gca,'xlabel'),'fontsize',16);
    set(get(gca,'ylabel'),'fontsize',16);
    set(get(gca,'title'),'fontsize',16);
    set(gca,'fontsize',18,'fontname','Times');
    %set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',1.5);
    h=legend('w=1.0','w=0.8','w=0.4','w=0.2','w=0.0','variable',2);
    set(h,'Fontsize',12);
    hold off;
    %v='pratio';
    %saveas(gcf,v,'eps');
    

