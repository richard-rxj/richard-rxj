cd D:\PhDWork\Jspace\Mobilesink\test\new\ImpactDiff50-11;

    %v = strcat(strcat('running_',int2str(i)),'.txt');
    Size=[100,200,300,400,500,600];
   
    
    set(gcf,'Position',[1 1 600 400]);
    %legend('Garg and K.','TPath',2);
    
   
    
    
    
    %subplot(2,1,1);
    C = load('unit-benefit-size-100.txt');
    N = C(:,1);
    [m,n]=size(N);
    RB = C(:,2);
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    h=bar(N,RB,'b');
    %title 'Min\_Benefit\_Cost';
    axis([0 100 0 45000]);
    
    
%     subplot(2,1,2);
%     C = load('unit-utility-size-100.txt');
%     N = C(:,1);
%     [m,n]=size(N);
%     RB = C(:,2);
%     %subplot(1,2,1);
%     %plot(N,CB,'-*r',N,WB,':pb');
%     h=bar(N,RB,'b');
%     title 'Min\_Utility\_Cost';
%     axis([0 100 0 50000]);
  
    %set(gca,'XTickLabel',{'0' '0.1' '0.2' '0.3' '0.4' '0.5' '0.6' '0.7' '0.8' '0.9' '1'});
   
    xlabel('Node Id');
    %ylabel('total flow per second(byte)');
    ylabel('Throughput');
    %title('Flow Comparison');
    
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    %set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);
    %v='pratio';
    %saveas(gcf,v,'eps');
    

