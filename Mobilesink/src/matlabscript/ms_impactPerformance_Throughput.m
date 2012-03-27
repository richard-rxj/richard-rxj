cd D:\PhDWork\Jspace\Mobilesink\test\dis\ImpactPerformance\T800;

    %v = strcat(strcat('running_',int2str(i)),'.txt');
    Size=[100,200,300,400,500,600];
    C = load('dis-benefitgain-tour.txt');
    N = C(:,1);
    [m,n]=size(N);
    RB = C(:,2);
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    h=plot(N,RB,'-dr');
    %set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerFaceColor',[0.113 0.020 0.025],'MarkerSize',10);
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 400]);
    %legend('Garg and K.','TPath',2);
    
    xlabel('Network Size');
    %ylabel('total flow per second(byte)');
    ylabel('Network Throughput');
    %title('Flow Comparison');
    
    hold on;
    C = load('max-benefitgain-tour.txt');
    RB = C(:,2);
    h=plot(N,RB,'-ob');
    set(h,'MarkerSize',12);
    
    C = load('random-benefitgain-tour.txt');
    RB = C(:,2);
    h=plot(N,RB,'-sk');
    set(h,'MarkerSize',12);
    
    
    
    legend('Max\_Gain','Max\_Throughput','Random\_Throughput',2);
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
    
% fid=fopen('D:\PhDWork\Jspace\Mobilesink\test\xmgracedata\performance-throughput-T100.txt','w');%写入文件路径
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
