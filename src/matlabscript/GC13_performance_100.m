cd D:\PhDWork\Jspace\globecom2013\test\ImpactPerformance(no enough data);

    %v = strcat(strcat('running_',int2str(i)),'.txt');
    %Size=[100,200,300,400,500,600];
    C = load('T1600.txt');
    N = C(:,1);
    RB = C(:,2);
    h=plot(N,RB,'-dr');
    %set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerFaceColor',[0.113 0.020 0.025],'MarkerSize',10);
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 400]);
    %legend('Garg and K.','TPath',2);
    
    xlabel('Network Size');
    %ylabel('total flow per second(byte)');
    ylabel('Network Data Quality');
    %title('Flow Comparison');
    
    hold on;
   
    RB = C(:,7);
    h=plot(N,RB,'-ob');
    set(h,'MarkerSize',12);
    
    RB = C(:,5);
    h=plot(N,RB,'-sk');
    set(h,'MarkerSize',12);
    
%     C = load('disOne-benefitgain-tour.txt');
%     RB = C(:,2);
%     h=plot(N,RB,'-^m');
%     set(h,'MarkerSize',12);
    
%     C = load('disTimTra-benefitgain-tour.txt');
%     RB = C(:,2);
%     h=plot(N,RB,'-xm');
%     set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerSize',12);
%     set(h,'MarkerSize',12);
%     
%     C = load('disTra-utilitygain-tour.txt');
%     RB = C(:,4);
%     E(:,5)=RB;
%     h=plot(N,RB,'-pm');
%     set(h,'MarkerSize',12);
%     set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    
    
    
    %axis([100 1000 0 300]);
    legend('Max\_Utility','Dis\_Max\_Utility','Random\_Utility',2);
  
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);
    %v='pratio';
    %saveas(gcf,v,'eps');
    
% fid=fopen('D:\PhDWork\Jspace\Mobilesink\test\xmgracedata\performance-goodput-T800.txt','w');%写入文件路径
% [m,n]=size(E); %获取矩阵的大小，p为要输出的矩阵
% for i=1:1:m
%   for j=1:1:n
%      if j==n %如果一行的个数达到n个则换行，否则空格
%         fprintf(fid,'%6.6f\n',E(i,j));
%     else
%        fprintf(fid,'%6.6f\t',E(i,j));
%     end
%   end
% end
%   fclose(fid); 
