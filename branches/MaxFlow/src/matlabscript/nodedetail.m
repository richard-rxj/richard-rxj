cd D:\PhDWork\Jspace\MaxFlow2012\test\data\nodedetail;

    %v = strcat(strcat('running_',int2str(i)),'.txt');
    Size=[100,200,300,400,500,600];
   
    
    set(gcf,'Position',[1 1 600 400]);
    %legend('Garg and K.','TPath',2);
    
   
    
    
    
    %subplot(2,1,1);
    C = load('wutility-N100-T0-I4-C6-R100.txt');
    N = C(:,6);
    %[m,n]=size(N);
    C = load('wutility-N100-T0-I4-C8-R80.txt');
    RB = C(:,6);
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    h=bar([N RB],'grouped');
    %title 'Min\_Benefit\_Cost';
    axis([0 100 0 110]);
    legend('unweighted allocation','weighted allocation');
    
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
   
    xlabel('Node Number');
    %ylabel('total flow per second(byte)');
    ylabel('Data Collected');
    %title('Flow Comparison');
    
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    %set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);
    %v='pratio';
    %saveas(gcf,v,'eps');
    

